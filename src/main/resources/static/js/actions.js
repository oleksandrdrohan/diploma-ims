function validateForm() {
    var cost = document.getElementById("cost").value;
    var quantity = document.getElementById("quantity").value;

    if (isNaN(cost) || isNaN(quantity)) {
        alert("Cost and Quantity must be numbers!");
        return false;
    }

    return true;
}

$(document).ready(function () {
    $("#deleteItemsForm").submit(function (event) {
        event.preventDefault();
        var itemIds = [];
        $("input.itemCheckbox:checked").each(function () {
            itemIds.push($(this).val());
        });

        $("#deleteItemsForm").append('<input type="hidden" name="itemIds" value="' + itemIds.join(',') + '">');


        $.ajax({
            url: "/delete-items",
            type: "POST",
            data: $("#deleteItemsForm").serialize(),
            success: function (response) {
                alert("Items deleted successfully");
                location.reload();
            },
            error: function () {
                alert("Error deleting items");
            }
        });
    });
});


$(document).ready(function () {
    $('#collectSelectedButton').on('click', function (event) {
        event.preventDefault();

        var selectedIds = [];
        $('.itemCheckbox:checked').each(function () {
            selectedIds.push($(this).val());
        });

        if (selectedIds.length === 0) {
            alert('Please select items to collect.');
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/collect-items',
            data: JSON.stringify(selectedIds),
            contentType: 'application/json',
            success: function (response) {

                var token = response.token;
                window.location.href = '/make-order?token=' + token;
            },
            error: function () {
                alert('An error occurred while collecting items.');
            },
        });
    });
});



