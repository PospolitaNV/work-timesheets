function showAlert(employee) {
    let confirmation = confirm("Вы уверены, что хотите удалить пользователя:" +
        "\n\nID: " + employee.id +
        "\nИмя: " + employee.firstName +
        "\nФамилия: " + employee.lastName
    );

    if (confirmation) {
        window.location.replace("/employee/delete?deleteId=" + employee.id)
    }

}
