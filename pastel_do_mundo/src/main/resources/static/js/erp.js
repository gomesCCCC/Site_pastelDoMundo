document.addEventListener('DOMContentLoaded', () => {
    setupMenu();
    loadPedidos();
    loadProdutos();
});

function setupMenu() {
    const buttons = document.querySelectorAll('.menu-btn');
    const sections = document.querySelectorAll('.section');

    buttons.forEach(btn => {
        btn.addEventListener('click', () => {
            const target = btn.getAttribute('data-section');

            sections.forEach(s => s.classList.remove('active'));
            document.getElementById(target).classList.add('active');

            buttons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            if (target === 'pedidos') {
                loadPedidos();
            }
        });
    });
}

async function loadPedidos() {
    const container = document.getElementById('pedidosContainer');
    container.innerHTML = '<div class="card">Carregando pedidos...</div>';

    try {
        const res = await fetch('/pedidos');

        if (!res.ok) {
            throw new Error(await res.text());
        }

        const pedidos = await res.json();

        if (!pedidos.length) {
            container.innerHTML = '<div class="card">Nenhum pedido encontrado.</div>';
            return;
        }

        container.innerHTML = pedidos.map(pedido => {
            const itens = (pedido.itens || []).map(item => `
                <div class="pedido-item">
                    <span>${escapeHtml(item.nomeProduto || 'Produto')}</span>
                    <span>x${item.quantidade}</span>
                    <span>R$ ${formatMoney(item.subtotal ?? item.precoUni)}</span>
                </div>
            `).join('');

            return `
                <div class="pedido-card">
                    <div class="pedido-card-header">
                        <div>
                            <strong>Pedido #${pedido.id}</strong>
                            <span>${formatDate(pedido.dataPedido)}</span>
                        </div>
                        <span class="status-badge status-${String(pedido.status).toLowerCase()}">${pedido.status}</span>
                    </div>

                    <div class="pedido-grid">
                        <div>
                            <strong>${escapeHtml(pedido.nomeCliente || 'Cliente')}</strong>
                            <span>${escapeHtml(pedido.telefoneCliente || 'Sem telefone')}</span>
                        </div>
                        <div>
                            <strong>Entrega</strong>
                            <span>${escapeHtml(pedido.enderecoEntrega || 'Endereco nao informado')}</span>
                        </div>
                        <div>
                            <strong>Total</strong>
                            <span>R$ ${formatMoney(pedido.total)}</span>
                        </div>
                    </div>

                    <div class="pedido-itens">
                        ${itens || '<div class="pedido-item">Sem itens registrados.</div>'}
                    </div>

                    <div class="pedido-actions">
                        <select onchange="updatePedidoStatus(${pedido.id}, this.value)">
                            ${statusOption('ABERTO', pedido.status)}
                            ${statusOption('PROCESSANDO', pedido.status)}
                            ${statusOption('FINALIZADO', pedido.status)}
                            ${statusOption('CANCELADO', pedido.status)}
                        </select>
                    </div>
                </div>
            `;
        }).join('');
    } catch (error) {
        console.error(error);
        container.innerHTML = '<div class="card">Erro ao carregar pedidos.</div>';
    }
}

window.updatePedidoStatus = async function (id, status) {
    const res = await fetch(`/pedidos/${id}/status?status=${status}`, {
        method: 'PATCH'
    });

    if (!res.ok) {
        showToast('Erro ao atualizar pedido', 'error');
        loadPedidos();
        return;
    }

    showToast('Pedido atualizado!', 'success');
    loadPedidos();
};

function statusOption(value, current) {
    return `<option value="${value}" ${value === current ? 'selected' : ''}>${value}</option>`;
}

function formatDate(value) {
    if (!value) return 'Sem data';
    return new Intl.DateTimeFormat('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    }).format(new Date(value));
}

function formatMoney(value) {
    return Number(value || 0).toFixed(2).replace('.', ',');
}

function escapeHtml(value) {
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

async function loadProdutos() {
    const res = await fetch('/api/produtos');
    const produtos = await res.json();

    const container = document.getElementById('estoqueTable');
    container.innerHTML = '';

produtos.forEach(p => {
    const isLow = p.quantidade < 10;

    container.innerHTML += `
        <div class="row">
            <div class="product-name">
                ${p.nome}
                ${isLow ? '<span class="stock-warning">quase esgotado</span>' : ''}
            </div>

            <div class="qty ${isLow ? 'low' : ''}">${p.quantidade}</div>

            <div class="actions">
                <button class="add" onclick="addStock(${p.id}, '${p.nome.replace(/'/g, "\\'")}')">+ estoque</button>
                <button class="remove" onclick="deleteProduct(${p.id})">remover</button>
            </div>
        </div>
    `;
});
}

window.createProduct = async function () {

const dto = {
    nome: document.querySelector('[name="nome"]').value,
    preco: Number(document.querySelector('[name="preco"]').value),
    categoria: document.querySelector('[name="categoria"]').value,
    URLimagem: document.querySelector('[name="URLimagem"]').value,
    descricao: document.querySelector('[name="descricao"]').value,
    quantidade: Number(document.querySelector('[name="quantidade"]').value),
    tamanho: document.querySelector('[name="tamanho"]').value?.charAt(0)
};

    const res = await fetch('/api/produtos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dto)
    });

    if (!res.ok) {
        console.error(await res.text());
        return;
    }

    clearForm();
    loadProdutos();
};

window.deleteProduct = async function (id) {
    await fetch(`/api/produtos/${id}`, {
        method: 'DELETE'
    });

    loadProdutos();
};

let selectedProductId = null;

window.addStock = function (id, nome) {
    selectedProductId = id;
    document.getElementById('qtyModalProductName').textContent = nome;

    const input = document.getElementById('qtyModalInput');
    input.value = 1;

    document.getElementById('qtyModalOverlay').classList.add('show');
    input.focus();
};

window.closeQtyModal = function () {
    document.getElementById('qtyModalOverlay').classList.remove('show');
    selectedProductId = null;
};

window.confirmAddStock = async function () {
    const input = document.getElementById('qtyModalInput');
    const quantidade = Number(input.value);

    if (!quantidade || quantidade <= 0) {
        showToast('Informe uma quantidade válida', 'error');
        return;
    }

    const res = await fetch(`/api/produtos/${selectedProductId}/estoque?quantidade=${quantidade}`, {
        method: 'PATCH'
    });

    if (!res.ok) {
        showToast('Erro ao atualizar estoque', 'error');
        return;
    }

    closeQtyModal();
    showToast('Estoque atualizado!', 'success');
    loadProdutos();
};

function clearForm() {
    document.querySelectorAll('.form-card input').forEach(i => i.value = '');
    const textarea = document.querySelector('.form-card textarea');
    if (textarea) textarea.value = '';
}

function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;

    container.appendChild(toast);

    requestAnimationFrame(() => toast.classList.add('show'));

    setTimeout(() => {
        toast.classList.remove('show');
        toast.addEventListener('transitionend', () => toast.remove(), { once: true });
    }, 5000);
}

window.createProduct = async function () {

    const dto = {
        nome: document.querySelector('[name="nome"]').value,
        preco: Number(document.querySelector('[name="preco"]').value),
        categoria: document.querySelector('[name="categoria"]').value,
        URLimagem: document.querySelector('[name="URLimagem"]').value,
        descricao: document.querySelector('[name="descricao"]').value,
        quantidade: Number(document.querySelector('[name="quantidade"]').value),
        tamanho: document.querySelector('[name="tamanho"]').value?.charAt(0) || null
    };

    const res = await fetch('/api/produtos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dto)
    });

    if (!res.ok) {
        const erro = await res.text();
        showToast('Erro ao cadastrar produto', 'error');
        console.error(erro);
        return;
    }

    showToast('Produto cadastrado com sucesso!', 'success');
    clearForm();
    loadProdutos();
};

async function loadProdutos() {
    const res = await fetch('/api/produtos');
    const produtos = await res.json();

    const container = document.getElementById('estoqueTable');
    container.innerHTML = '';

    produtos.forEach(p => {
        const isLow = p.quantidade < 10;
        const isInactive = !p.ativo;

        container.innerHTML += `
            <div class="row ${isInactive ? 'inactive' : ''}">
                <div class="product-name">
                    ${p.nome}
                    ${isLow ? '<span class="stock-warning">quase esgotado</span>' : ''}
                </div>

                <div class="qty ${isLow ? 'low' : ''}">${p.quantidade}</div>

                <div class="actions">
                    <button class="add" onclick="addStock(${p.id}, '${p.nome.replace(/'/g, "\\'")}')">+ estoque</button>
                    <button class="toggle-btn ${isInactive ? 'reativar' : ''}" onclick="toggleDisponibilidade(${p.id})">
                        ${isInactive ? 'reativar' : 'desativar'}
                    </button>
                    <button class="remove" onclick="deleteProduct(${p.id})">remover</button>
                </div>
            </div>
        `;
    });
}

window.toggleDisponibilidade = async function (id) {
    const res = await fetch(`/api/produtos/${id}/disponibilidade`, {
        method: 'PATCH'
    });

    if (!res.ok) {
        showToast('Erro ao atualizar disponibilidade', 'error');
        return;
    }

    const produto = await res.json();
    showToast(
        produto.ativo ? 'Produto reativado no cardápio' : 'Produto removido do cardápio',
        produto.ativo ? 'success' : 'error'
    );
    loadProdutos();
};
