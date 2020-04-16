function includeHtml(id, path) {
    fetch(path)
        .then(response => {
            return response.text()
        })
        .then(data => {
            document.querySelector(`#${id}`).innerHTML = data;
        })
    document.querySelector(`#${id}`).innerHTML = `<object type="text/html" data=\"${path}\">`;
}
includeHtml("header", "fragments/header.html");
includeHtml("footer", "fragments/footer.html");