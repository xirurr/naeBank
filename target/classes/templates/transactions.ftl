<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>
<#import "parts/common.ftl" as c>

<@c.page>
    <@p.pager url page/>
    <table class="table" id="transaction-list">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">DATE</th>
            <th scope="col">AMMOUNT</th>
            <th scope="col">SENDER</th>
            <th scope="col">RECIEVER</th>
        </tr>
        </thead>
        <tbody>
        <#list page.content as trans>
            <tr>
                <th scope="row">${trans.id}</th>
                <td>${trans.date}</td>
                <td>${trans.ammount}</td>
                <td>${trans.sender.getUsername()}(${trans.sender.getId()})</td>
                <td>${trans.reciever.getUsername()}(${trans.reciever.getId()})</td>
            </tr>
        <#else>
            No transactions
        </#list>
        </tbody>
    </table>

    <@p.pager url page/>
</@c.page>
