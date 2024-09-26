var baseURL = `http://localhost:8081/api/contacts`;
console.log("contact-details fetch js page.");
async function launchModelWithContactDetails(contactId) {
    console.log("contact id: " + contactId);

    try {
        const response = await fetch(baseURL + "/" + contactId);
        if (!response.ok) {
            throw new Error(`Error: ${response.status}: ${response.statusText}`);
        }
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
