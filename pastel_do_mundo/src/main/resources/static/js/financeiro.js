let financeiroCarregado = false;


window.setupFinanceiro = function(){

    const select =
        document.getElementById('periodoFinanceiro');


    if(!select) return;


    select.addEventListener('change', () => {

        loadFinanceiro();

    });

};



window.loadFinanceiro = async function(){

    const periodo =
    document.getElementById('periodoFinanceiro').value;


    const container =
    document.getElementById('financeiroCards');


    container.innerHTML =
    `
    <div class="finance-card loading">
        Carregando...
    </div>
    `;


    try {


        const response = await fetch(
            `/financeiro/relatorio?periodo=${periodo}`
        );


        if(!response.ok){

            throw new Error(
                await response.text()
            );

        }


        const dados =
        await response.json();



        renderFinanceiro(dados);



    }catch(error){

        console.error(error);


        container.innerHTML =
        `
        <div class="finance-card">
            Erro ao carregar financeiro
        </div>
        `;

    }


}



function renderFinanceiro(dados){


    const cards =
    document.getElementById('financeiroCards');


    cards.innerHTML = `


    <div class="finance-card">

        <h3>Pedidos</h3>

        <strong>
            ${dados.quantidadePedidos}
        </strong>

    </div>



    <div class="finance-card faturamento">

        <h3>Faturamento bruto</h3>

        <strong>
            R$ ${formatMoney(dados.faturamentoBruto)}
        </strong>

    </div>



    <div class="finance-card taxas">

        <h3>Taxas</h3>

        <strong>
            R$ ${formatMoney(dados.totalTaxas)}
        </strong>

    </div>



    <div class="finance-card liquido">

        <h3>Valor líquido</h3>

        <strong>
            R$ ${formatMoney(dados.valorLiquido)}
        </strong>

    </div>


    `;


    renderCanais(dados.porCanal);

}



function renderCanais(canais){


    const container =
    document.getElementById('financeiroCanais');


    if(!canais || canais.length === 0){

        container.innerHTML =
        `
        <div class="card">
            Nenhuma venda encontrada
        </div>
        `;

        return;

    }



    container.innerHTML =
    canais.map(canal => `


        <div class="canal-card">


            <div class="canal-header">

                <span>
                    ${canal.canal}
                </span>

                <span>
                    ${canal.quantidadePedidos}
                    pedidos
                </span>

            </div>



            <div class="canal-grid">


                <div class="canal-info">

                    <span>Bruto</span>

                    <strong>
                    R$ ${formatMoney(canal.faturamentoBruto)}
                    </strong>

                </div>



                <div class="canal-info">

                    <span>Taxas</span>

                    <strong>
                    R$ ${formatMoney(canal.totalTaxas)}
                    </strong>

                </div>



                <div class="canal-info">

                    <span>Líquido</span>

                    <strong>
                    R$ ${formatMoney(canal.valorLiquido)}
                    </strong>

                </div>


            </div>


        </div>


    `).join('');

}



function formatMoney(value){

    return Number(value || 0)
        .toFixed(2)
        .replace('.',',');

}