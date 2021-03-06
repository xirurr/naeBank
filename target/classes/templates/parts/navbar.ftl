<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Greeting</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/transactions">Transactions</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/accounts">accounts</a>
                </li>
            </#if>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/users">User-list<b>(admin)</b></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/transactions/all">Transactions-all<b>(admin)</b></a>
                </li>
            </#if>
        </ul>
        <div class="navbar-text mr-3">${name}</div>
            <@l.logout/>
    </div>


</nav>