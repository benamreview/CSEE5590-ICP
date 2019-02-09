function getGithubInfo(user) {
    //1. Create an instance of XMLHttpRequest class and send a GET request using it. The function should finally return the object(
    // it now contains the response!)
    var xhttp =new XMLHttpRequest();
    var url = "https://api.github.com/search/users?q=" + user;
    xhttp.open('GET', url, false); //synchronous
    xhttp.send();
    return xhttp;
}

function showUser(user) {

    //2. set the contents of the h2 and the two div elements in the div '#profile' with the user content
    $(".avatar").empty();
    $(".information").empty();
    console.log("in showUser");
    console.log(user);
    console.log(user.items[0].avatar_url);
    $("#name").text("Username: " + user.items[0].login);
    var avatarUrl = user.items[0].avatar_url;
    var avatarImg = "<img src=\"" + avatarUrl + "\" alt=\"Trulli\" width=\"500\" height=\"333\"/>"
    console.log(avatarImg);
    $(".avatar").append(avatarImg);
    var informationStr = "<p style= \'font-size: 40px; font-weight: bold; \'>Profile Information</p><p> User ID: " + user.items[0].id + "</p><p>User's Score: " + user.items[0].score + "</p>"
    $(".information").append(informationStr);
    var linkStr = "<p><a style = \"color:blue\" href=\"" + user.items[0].html_url + "\" target=\"_blank\" >User's Github URL</a></p>"
    console.log(linkStr);
    $(".information").append(linkStr);

}

function noSuchUser(username) {
    //3. set the elements such that a suitable message is displayed
    console.log("in noSuchUser");
    $("#name").text("ERROR: USER NOT FOUND!");

}


$(document).ready(function(){
    $(document).on('keypress', '#username', function(e){
        //check if the enter(i.e return) key is pressed
        if (e.which == 13) {
            //get what the user enters
            username = $(this).val();
            //reset the text typed in the input
            $(this).val("");
            //get the user's information and store the respsonse
            response = getGithubInfo(username);
            console.log("this is response");
            //if the response is successful show the user's details
            if (response.status == 200) {
                showUser(JSON.parse(response.responseText));
                //else display suitable message
            } else {
                noSuchUser(username);
            }
        }
    })
});
