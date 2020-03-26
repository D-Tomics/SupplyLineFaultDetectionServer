function onLoad() {
    if(checkCookie("loaded")) return;
    addCookie("loaded","true");
    document.getElementsByTagName("body")[0].classList.add("fadeIn");
}

function checkCookie(name) {
    return accessCookie(name) != "";
}

function accessCookie(name) {
    var cookieName = name+"=";
    var allCookies = document.cookie.split(";");
    for(var i=0; i < allCookies.length; i++) {
        var temp = allCookies[i].trim();
        if(temp.indexOf(cookieName) == 0)
            return temp.substring(temp.indexOf(cookieName),temp.length);
    }
    return "";
}

function addCookie(name, value) {
    document.cookie = name+"="+value+";";
} 