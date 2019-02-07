<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>
<#import "parts/actions.ftl" as act>

<@c.page>

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

<@act.newAcc userD.id/>