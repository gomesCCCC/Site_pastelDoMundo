document.addEventListener('DOMContentLoaded', () => {
    setupMenu();
    loadPedidos();
    setupFinanceiro();
    setupModalDismissal();
    setupMotivoModalConfirm();
});

function setupMenu() {

    const buttons = document.querySelectorAll('.menu-btn');
    const sections = document.querySelectorAll('.section');


    buttons.forEach(btn => {

        btn.addEventListener('click', () => {


            const target = btn.getAttribute('data-section');


            sections.forEach(section => {

                section.classList.remove('active');

            });


            document
                .getElementById(target)
                .classList.add('active');



            buttons.forEach(button => {

                button.classList.remove('active');

            });


            btn.classList.add('active');



            switch(target) {


                case 'pedidos':

                    loadPedidos();

                    break;



                case 'estoque':

                    loadProdutos();

                    break;



                case 'financeiro':

                    loadFinanceiro();

                    break;


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
    ${renderPedidoActions(pedido)}
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
    const res = await fetch('/api/produtos', { cache: 'no-store' });
    const produtos = await res.json();

    const container = document.getElementById('estoqueTable');
    container.innerHTML = '';

    produtos.forEach(p => {
        const isLow = p.quantidade < 10;
        const isInactive = !p.ativo;

        const row = document.createElement('div');
        row.className = `row ${isInactive ? 'inactive' : ''}`;

        const productName = document.createElement('div');
        productName.className = 'product-name';
        productName.append(document.createTextNode(p.nome));

        if (isLow) {
            const warning = document.createElement('span');
            warning.className = 'stock-warning';
            warning.textContent = 'quase esgotado';
            productName.appendChild(warning);
        }

        const qty = document.createElement('div');
        qty.className = `qty ${isLow ? 'low' : ''}`;
        qty.textContent = p.quantidade;

        const actions = document.createElement('div');
        actions.className = 'actions';

        const addButton = document.createElement('button');
        addButton.className = 'add';
        addButton.textContent = '+ estoque';
        addButton.addEventListener('click', () => addStock(p.id, p.nome));

        const toggleButton = document.createElement('button');
        toggleButton.className = `toggle-btn ${isInactive ? 'reativar' : ''}`;
        toggleButton.textContent = isInactive ? 'reativar' : 'desativar';
        toggleButton.addEventListener('click', () => toggleDisponibilidade(p.id, p.nome, isInactive));

        const removeButton = document.createElement('button');
        removeButton.className = 'remove';
        removeButton.textContent = 'remover';
        removeButton.addEventListener('click', () => deleteProduct(p.id, p.nome));

        actions.append(addButton, toggleButton, removeButton);
        row.append(productName, qty, actions);
        container.appendChild(row);
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
        tamanho: document.querySelector('[name="tamanho"]').value?.charAt(0) || null
    };

    const res = await fetch('/api/produtos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
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

function clearForm() {
    document.querySelectorAll('.form-card input').forEach(i => i.value = '');
    const textarea = document.querySelector('.form-card textarea');
    if (textarea) textarea.value = '';
}


let selectedProductId = null;

window.addStock = function (id, nome) {
    selectedProductId = id;
    document.getElementById('qtyModalProductName').textContent = nome;
    document.getElementById('qtyModalInput').value = 1;
    document.getElementById('qtyModalMotivo').value = '';

    document.getElementById('qtyModalOverlay').classList.add('show');
    document.getElementById('qtyModalInput').focus();
};

window.closeQtyModal = function () {
    document.getElementById('qtyModalOverlay').classList.remove('show');
    selectedProductId = null;
};

window.confirmAddStock = async function () {
    const quantidade = Number(document.getElementById('qtyModalInput').value);
    const motivo = document.getElementById('qtyModalMotivo').value.trim();

    if (!quantidade || quantidade <= 0) {
        showToast('Informe uma quantidade válida', 'error');
        return;
    }

    const params = new URLSearchParams({ quantidade });
    if (motivo) params.set('motivo', motivo);

    const res = await fetch(`/api/produtos/${selectedProductId}/estoque?${params}`, {
        method: 'PATCH'
    });

    if (!res.ok) {
        showToast(await getErrorMessage(res, 'Erro ao atualizar estoque'), 'error');
        return;
    }

    closeQtyModal();
    showToast('Estoque atualizado!', 'success');
    await loadProdutos();
};


let motivoModalCallback = null;

function openMotivoModal(title, nome, onConfirm) {
    document.getElementById('motivoModalTitle').textContent = title;
    document.getElementById('motivoModalProductName').textContent = nome;
    document.getElementById('motivoModalInput').value = '';
    motivoModalCallback = onConfirm;

    document.getElementById('motivoModalOverlay').classList.add('show');
    document.getElementById('motivoModalInput').focus();
}

window.closeMotivoModal = function () {
    document.getElementById('motivoModalOverlay').classList.remove('show');
    motivoModalCallback = null;
};

function setupMotivoModalConfirm() {
    document.getElementById('motivoModalConfirmBtn').addEventListener('click', async () => {
        const motivo = document.getElementById('motivoModalInput').value.trim();

        const callback = motivoModalCallback;
        closeMotivoModal();
        if (callback) await callback(motivo);
    });
}

window.deleteProduct = function (id, nome) {
    openMotivoModal('Remover produto', nome, async (motivo) => {
        const params = new URLSearchParams();
        if (motivo) params.set('motivo', motivo);

        const res = await fetch(`/api/produtos/${id}?${params}`, { method: 'DELETE' });

        if (!res.ok) {
            showToast(await getErrorMessage(res, 'Erro ao remover produto'), 'error');
            return;
        }

        showToast('Produto removido', 'success');
        await loadProdutos();
    });
};

window.toggleDisponibilidade = function (id, nome, isInactive) {
    const titulo = isInactive ? 'Reativar produto' : 'Desativar produto';

    openMotivoModal(titulo, nome, async (motivo) => {
        const params = new URLSearchParams();
        if (motivo) params.set('motivo', motivo);

        const res = await fetch(`/api/produtos/${id}/disponibilidade?${params}`, { method: 'PATCH' });

        if (!res.ok) {
            showToast(await getErrorMessage(res, 'Erro ao atualizar disponibilidade'), 'error');
            return;
        }

        const produto = await res.json();
        showToast(
            produto.ativo ? 'Produto reativado no cardápio' : 'Produto removido do cardápio',
            produto.ativo ? 'success' : 'error'
        );
        await loadProdutos();
    });
};


window.openHistorico = async function () {
    const overlay = document.getElementById('historicoModalOverlay');
    const list = document.getElementById('historicoList');

    overlay.classList.add('show');
    list.innerHTML = '<div class="historico-empty">Carregando...</div>';

    const res = await fetch('/estoque/historico', { cache: 'no-store' });

    if (!res.ok) {
        list.innerHTML = '<div class="historico-empty">Erro ao carregar histórico</div>';
        return;
    }

    const movimentacoes = await res.json();

    if (movimentacoes.length === 0) {
        list.innerHTML = '<div class="historico-empty">Nenhuma movimentação registrada ainda</div>';
        return;
    }

    list.innerHTML = movimentacoes.map(m => `
        <div class="historico-item">
            <div class="historico-item-top">
                <span>${m.produtoNome}</span>
                <span class="historico-tipo ${m.tipo}">${m.tipo}</span>
            </div>
            <div class="historico-item-meta">
                ${formatarData(m.data)} • quantidade: ${m.quantidade}
            </div>
            ${m.motivo ? `<div class="historico-item-motivo">${m.motivo}</div>` : ''}
        </div>
    `).join('');
};

window.closeHistorico = function () {
    document.getElementById('historicoModalOverlay').classList.remove('show');
};

function formatarData(isoString) {
    const data = new Date(isoString);
    return data.toLocaleString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}


function setupModalDismissal() {
    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) overlay.classList.remove('show');
        });
    });

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            document.querySelectorAll('.modal-overlay.show').forEach(o => o.classList.remove('show'));
        }
    });
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

async function getErrorMessage(res, fallback) {
    const details = await res.text();
    return details ? `${fallback}: ${details}` : fallback;
}

function renderPedidoActions(pedido) {

    switch(pedido.status) {

        case "ABERTO":
            return `
                <button onclick="updatePedidoStatus(${pedido.id}, 'PROCESSANDO')">
                    Aceitar pedido
                </button>

                <button onclick="updatePedidoStatus(${pedido.id}, 'CANCELADO')">
                    Cancelar
                </button>
            `;


        case "PROCESSANDO":
            return `
                <button onclick="updatePedidoStatus(${pedido.id}, 'FINALIZADO')">
                    Finalizar preparo
                </button>
            `;


        case "FINALIZADO":
            return `
                <span>Pedido finalizado</span>
            `;


        case "CANCELADO":
            return `
                <span>Pedido cancelado</span>
            `;
    }
}