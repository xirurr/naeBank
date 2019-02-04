<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">User Name :</label>
            <div class="col-sm-5">
                <input class="form-control ${(usernameError??)?string('is-invalid','')}"
                       type="text" name="username" placeholder="Username"
                       value="<#if user??>${user.username}</#if>"
                />
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-5">
                <input class="form-control ${(passwordError??)?string('is-invalid','')}" type="password" name="password"
                       placeholder="Password"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <#if isRegisterForm>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Password:</label>
                <div class="col-sm-5">
                    <input class="form-control ${(password2Error??)?string('is-invalid','')}" type="password"
                           name="password2" placeholder="retype Password"/>
                    <#if password2Error??>
                        <div class="invalid-feedback">
                            ${password2Error}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Дата рождения:</label>
                    <div class="form-group">
                        <input class="form-control" type="date" name="dateOfBirth">
                    </div>
            </div>
        </#if>
        <#if !isRegisterForm>
            <a href="/registration">Registration</a>
        </#if>
        <button class="btn btn-primary" type="submit"><#if isRegisterForm>SIGN UP<#else>SIGN IN</#if></button>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <button class="btn btn-primary" type="submit"><#if user??>Sign Out<#else>Log in</#if></button>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>