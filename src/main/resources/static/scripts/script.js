console.log("script Invoked!!!")
retainThemeOnPageChangeOrReload();
document.querySelector("#toggle-theme-btn").addEventListener("click", themeChanger);

function themeChanger() {
    // 1:light, 0:dark
    let currentTheme = localStorage.getItem("theme") ?? 1;
    let themeText = document.querySelector("#toggle-theme-btn span");
    let htmlTag = document.querySelector("html");

    console.log("curent theme: " + currentTheme + " and text: " + themeText.innerHTML);
    if (currentTheme == 1) {
        htmlTag.classList.add("dark");
        console.log("changing to dark theme");
        localStorage.setItem("theme", 0);
        themeText.innerHTML = "Light";
    } else {
        htmlTag.classList.remove("dark");
        console.log("changing to light theme");
        localStorage.setItem("theme", 1);
        themeText.innerHTML = "Dark";
    }
}

function retainThemeOnPageChangeOrReload() {
    let currentTheme = localStorage.getItem("theme") ?? 1;
    if (currentTheme == 1) return;
    let themeText = document.querySelector("#toggle-theme-btn span");
    let htmlTag = document.querySelector("html");
    if (currentTheme == 0 && !htmlTag.classList.contains("dark")) {
        htmlTag.classList.add("dark");
        themeText.innerHTML = "Light";
    }
}
