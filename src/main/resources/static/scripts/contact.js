var baseURL = `http://localhost:8081`;
console.log("contact js page invoked.");
async function launchModelWithContactDetails(contactId) {
    console.log("details to be fetched - contact id: " + contactId);
    try {
        const response = await fetch(`${baseURL}/api/contacts/${contactId}`);
        if (!response.ok) throw new Error(`Error: ${response.status}: ${response.statusText}`);
        console.log("response: " + response);
        var contact = await response.json();
        console.log("contact object: " + contact);
        // setting up image and other details
        document.getElementById('contact-img').setAttribute('src', contact["picture"] ?? "");
        document.getElementById('contact-name').innerHTML = contact["name"] ?? "NA";
        document.getElementById('contact-email').innerHTML = contact["email"] ?? "NA";
        document.getElementById('contact-phoneNumber').innerHTML = contact["phoneNumber"] ?? "NA";
        document.getElementById('contact-address').innerHTML = contact["address"] ?? "NA";
        document.getElementById('contact-description').innerHTML = contact["description"] ?? "NA";
        document.getElementById('contact-websiteLink').innerHTML = contact["websiteLink"] ?? "NA";
        document.getElementById('contact-linkedInLink').innerHTML = contact["linkedInLink"] ?? "NA";
        document.getElementById('contact-isFavourite').innerHTML = contact["isFavourite"] ?? "NA";

    } catch (error) {
        console.error('Error fetching contact details:', error);
        alert("error in fetching contact details!")
    }
}


// Delete contact
function deleteContact(contactId) {
    console.log(`contact to be deleted - contact id: ${contactId}`);
    try {
        Swal.fire({
            title: "Are you sure?",
            text: "You won't be able to revert this!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!"
        }).then(async (result) => {
            if (result.isConfirmed) {
                const url = `${baseURL}/user/contacts/delete/${contactId}`;
                window.location.replace(url);

            }
        });
    } catch (error) {
        console.error('Error fetching contact details:', error);
        alert("error in deleting contact details!")
    }

}
