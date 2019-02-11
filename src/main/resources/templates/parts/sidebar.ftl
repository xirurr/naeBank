<#import "common.ftl" as c>
<#import "actions.ftl" as act>
<#macro sidebar>
    <@c.page>
<div class="wrapper">
    <!-- Sidebar  -->
    <nav id="sidebar" name="sidebarNewAcc">
        <div class="sidebar-header">
            <h3></h3>
        </div>
            <@act.newAcc/>
    </nav>
    <nav id="sidebar">
        <div class="sidebar-header" name="sidebarNewTrans">
            <h3></h3>
        </div>
        <ul class="list-unstyled components">
            <p>Dummy Heading</p>
            <@act.transactTo/>
        </ul>
    </nav>
    <nav id="sidebar">
        <div class="sidebar-header" name="sidebarAddMoney">
            <h3></h3>
        </div>
        <ul class="list-unstyled components">
            <p>Dummy Heading</p>
            <@act.addMoney/>
        </ul>
    </nav>

    <!-- Page Content  -->
    <div id="content">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <button type="button" id="sidebarNewAccCollapse" class="btn btn-info">
                    <i class="fas fa-align-left"></i>
                    <span>new Acc</span>
                </button>
                <button type="button" id="sidebarAddMoneyCollapse" class="btn btn-info">
                    <i class="fas fa-align-left"></i>
                    <span>Toggle Sidebar</span>
                </button>
                <button type="button" id="sidebarNewTransCollapse" class="btn btn-info">
                    <i class="fas fa-align-left"></i>
                    <span>Toggle Sidebar</span>
                </button>
            </div>

        </nav>
        <#nested>
    </div>
</div>
    </@c.page>
    <script>
        $(document).ready(function () {
        /*    $('#sidebarNewAccCollapse').on('click', function () {
                $('nav[name=sidebarNewAcc]').toggleClass('active');
            });*/
            $('#sidebarAddMoneyCollapse').on('click', function () {
                $('nav[name=sidebarAddMoney]').toggleClass('active');
            });
            $('#sidebarNewTransCollapse').on('click', function () {
                $('nav[name=sidebarNewTrans]').toggleClass('active');
            });
        });
    </script>

</#macro>
