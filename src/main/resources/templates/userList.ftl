<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>
<#import "parts/common.ftl" as c>

<@c.page>
    <@p.pager url page/>
    <table class="table" id="transaction-list">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">username</th>
            <th scope="col">AGE</th>
            <th scope="col">Date of birth</th>
            <th scope="col">Transactions</th>
            <th scope="col">Accounts</th>
            <th scope="col">Summary</th>

        </tr>
        </thead>
        <tbody>
        <#list page.content as user>
            <tr>
                <th scope="row">${user.id}</th>
                <td><a href="/accounts/${user.id}">${user.username}</td>
                <td>${user.age}</td>
                <td>${user.getDateOfBirth()}</td>
                <td><a href="/transactions/${user.id}"> transactions list</a></td>
                <td><a href="/accounts/${user.id}"> accounts list</a></td>
                <td>${user.summ}</td>
            </tr>
        </#list>
        </tbody>
    </table>
    <@p.pager url page/>
</@c.page>

