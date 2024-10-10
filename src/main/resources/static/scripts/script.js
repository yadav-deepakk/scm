console.log("script Invoked!!!")
retainThemeOnPageChangeOrReload();

function themeChanger() {
    // 1:light, 0:dark
    let currentTheme = localStorage.getItem("theme") ?? 1;
    let htmlTag = document.querySelector("html");

    console.log("curent theme: " + currentTheme);
    if (currentTheme == 1) {
        htmlTag.classList.add("dark");
        console.log("changing to dark theme");
        localStorage.setItem("theme", 0);
    } else {
        htmlTag.classList.remove("dark");
        console.log("changing to light theme");
        localStorage.setItem("theme", 1);
    }
}

function retainThemeOnPageChangeOrReload() {
    let currentTheme = localStorage.getItem("theme") ?? 1;
    if (currentTheme == 1) return;
    let htmlTag = document.querySelector("html");
    if (currentTheme == 0 && !htmlTag.classList.contains("dark")) {
        htmlTag.classList.add("dark");
    }
}
