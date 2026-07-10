document.addEventListener('DOMContentLoaded', () => {

    setupFinanceiro();

});


function setupFinanceiro() {

    const botaoFinanceiro =
        document.querySelector('[data-section="financeiro"]');

    if (!botaoFinanceiro) {
        return;
    }

    botaoFinanceiro.addEventListener('click', () => {
        loadFinanceiro();
    });

    const seletorPeriodo = document.getElementById('periodoFinanceiro');

    if (seletorPeriodo) {
        seletorPeriodo.addEventListener('change', () => {
            loadFinanceiro();
        });
    }
}


function getPeriodoSelecionado() {
    const seletor = document.getElementById('periodoFinanceiro');
    return seletor ? seletor.value : 'MES';
}


async function loadFinanceiro() {

    const cards = document.getElementById('financeiroCards');

    if (!cards) {
        console.error('Elemento financeiroCards não encontrado');
        return;
    }

    cards.innerHTML = `
    <div class="card">
        Carregando financeiro...
    </div>
    `;

    const canaisContainer = document.getElementById('financeiroCanais');

    if (canaisContainer) {
        canaisContainer.innerHTML = `
        <div class="card loading">
            Carregando...
        </div>
        `;
    }

    try {

        const periodo = getPeriodoSelecionado();

        const response = await fetch(
            `/financeiro/relatorio?periodo=${periodo}`
        );

        if (!response.ok) {
            throw new Error(await response.text());
        }

        const dados = await response.json();

        renderFinanceiro(dados);

    } catch (error) {

        console.error('Erro financeiro:', error);

        cards.innerHTML = `
        <div class="card">
            Erro ao carregar financeiro
        </div>
        `;

        if (canaisContainer) {
            canaisContainer.innerHTML = '';
        }
    }
}


function renderFinanceiro(dados) {

    const cards = document.getElementById('financeiroCards');

    cards.innerHTML = `

    <div class="finance-card">
        <h3>Pedidos</h3>
        <strong>${dados.quantidadePedidos ?? 0}</strong>
    </div>

    <div class="finance-card faturamento">
        <h3>Faturamento bruto</h3>
        <strong>R$ ${formatMoney(dados.faturamentoBruto)}</strong>
    </div>

    <div class="finance-card taxas">
        <h3>Taxas</h3>
        <strong>R$ ${formatMoney(dados.totalTaxas)}</strong>
    </div>

    <div class="finance-card liquido">
        <h3>Valor líquido</h3>
        <strong>R$ ${formatMoney(dados.valorLiquido)}</strong>
    </div>

    `;

    renderCanais(dados.porCanal);
}


function renderCanais(canais) {

    const container = document.getElementById('financeiroCanais');

    if (!container) {
        return;
    }

    if (!canais || canais.length === 0) {

        container.innerHTML = `
        <div class="card">
            Nenhuma venda encontrada
        </div>
        `;

        return;
    }

    container.innerHTML = canais.map(canal => `

        <div class="canal-card">

            <div class="canal-header">
                <strong>${formatCanal(canal.canal)}</strong>
                <span>${canal.quantidadePedidos} pedidos</span>
            </div>

            <div class="canal-grid">

                <div>
                    <span>Bruto</span>
                    <strong>R$ ${formatMoney(canal.faturamentoBruto)}</strong>
                </div>

                <div>
                    <span>Taxas</span>
                    <strong>R$ ${formatMoney(canal.totalTaxas)}</strong>
                </div>

                <div>
                    <span>Líquido</span>
                    <strong>R$ ${formatMoney(canal.valorLiquido)}</strong>
                </div>

            </div>

        </div>

    `).join('');
}


function formatCanal(canal) {
    const nomes = {
        SITE_PROPRIO: 'Site Próprio',
        IFOOD: 'iFood'
    };

    return nomes[canal] || canal;
}