console.log("contact js page invoked.");

var baseURL = `http://localhost:8081`;

// modal data
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

// download xls file
async function downloadExcel(userId) {
    try {
        const worksheetData = [];
        const response = await fetch(`${baseURL}/api/xls/contacts`);
        const data = await response.json();
        const headers = Object.keys(data[0]);
        worksheetData.push(headers);
        data.forEach(item => worksheetData.push(Object.values(item)));
        const worksheet = XLSX.utils.aoa_to_sheet(worksheetData);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "API Data");
        XLSX.writeFile(workbook, `contactList_${getCurrentDateTime()}.xlsx`);
    } catch (error) {
        console.error("Error fetching or processing data:", error);
    }
}

function getCurrentDateTime() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const hours = String(today.getHours()).padStart(2, '0');
    const minutes = String(today.getMinutes()).padStart(2, '0');
    const seconds = String(today.getSeconds()).padStart(2, '0');
    return `${year}${month}${day}_${hours}${minutes}${seconds}`; // Returns YYYYMMDD_HHMMSS
}