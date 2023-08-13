const TokenKey = 'reggie_token'

function getToken() {
    return localStorage.getItem(TokenKey);
}

function setToken(token) {
    return localStorage.setItem(TokenKey,token);
}

function removeToken() {
    return localStorage.removeItem(TokenKey);
}
