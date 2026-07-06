document.addEventListener('DOMContentLoaded', () => {
    setupMenu();
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
        });
    });
}

async function loadProdutos() {
    const res = await fetch('/api/produtos');
    const produtos = await res.json();

    const container = document.getElementById('estoqueTable');
    container.innerHTML = '';

    produtos.forEach(p => {
        container.innerHTML += `
            <div class="row">
                <div class="product-name">${p.nome}</div>

                <div class="qty">${p.quantidade}</div>

                <div class="actions">
                    <button class="add" onclick="addStock(${p.id})">+ estoque</button>
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

window.addStock = async function (id) {
    await fetch(`/api/produtos/${id}/estoque?quantidade=1`, {
        method: 'PATCH'
    });

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