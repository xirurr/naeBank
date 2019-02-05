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
            <#if (user.id=trans.sender.id || user.id=trans.reciever.id)>
                <tr>
                    <th scope="row">${trans.id}</th>
                    <td>${trans.date}</td>
                    <td>${trans.ammount}</td>
                    <td>${trans.sender.getUsername()}(${trans.sender.getId()})</td>
                    <td>${trans.reciever.getUsername()}(${trans.reciever.getId()})</td>
                </tr>
            </#if>
        <#else>
            Part executed when there are 0 items
        </#list>
        </tbody>
    </table>
    <@p.pager url page/>
</@c.page>
