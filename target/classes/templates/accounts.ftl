<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>
<#import "parts/actions.ftl" as act>


<@c.page>
<div >
    <a class="btn btn-primary" data-toggle="collapse" href="#addAccForm" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        добавить рассчетный счет
    </a>
    <div class="collapse" id="addAccForm">
            <div class="form-inline">
    <@act.newAcc userD.id/>
            </div>
        </div>
</div>
    <table class="table" id="transaction-list">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col">TAG</th>
        <th scope="col">AMMOUNT</th>

    </tr>
    </thead>
    <tbody>
    <#list list as account>
        <tr>
            <th scope="row">${account.id}</th>
            <td>${account.tag!"nonTagged"}</td>
            <td>${account.ammount}</td>
        </tr>
    <#else>
        Транзакций нет
    </#list>


</@c.page>
