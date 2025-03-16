$(document).ready(function () {
    $('#zips').dataTable({
        searching: false
    });
    $('#loaded').css('display', 'block');
    $('#loading').css('display', 'none');
});

function deleteZip(name, id) {
    let conf = confirm(`Are you sure to delete the selected zip code ${id} of ${name}?`);
    if (!conf) {
        return;
    }
    $.ajax({
        url: "/delete",
        type: 'post',
        data: {
            id: id
        },
        success: function () {
            location.href = '/';
        },
        error: function () {
            alert('Zip code not found');
        }
    });
}