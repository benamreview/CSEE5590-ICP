/**
 *
 * @param user
 * @returns {XMLHttpRequest}
 */
function getGithubInfo(user) {
    //1. Create an instance of XMLHttpRequest class and send a GET request using it. The function should finally return the object(
    // it now contains the response!)
    var xhttp =new XMLHttpRequest();
    var url = "https://api.github.com/users/" + user;
    xhttp.open('GET', url, false); //synchronous
    xhttp.send();
    return xhttp;
}

/**
 *
 * @param user: show basic user information, number of public repositories, and his/her github url
 */
function showUser(user) {

    //2. set the contents of the h2 and the two div elements in the div '#profile' with the user content
    //Empty the avatar and information content
    $(".avatar").empty();
    $(".information").empty();
    //Display name
    $("#name").text("Username: " + user.login);
    //Avatar URL
    var avatarUrl = user.avatar_url;
    var avatarImg = "<img src=\"" + avatarUrl + "\" alt=\"Trulli\" width=\"500\" height=\"333\"/>"
    console.log(avatarImg);
    $(".avatar").append(avatarImg);
    //User information
    var informationStr = "<p style= \'font-size: 40px; font-weight: bold; \'>Profile Information</p><p> User ID: " + user.id + "</p><p>Number of Repositories: " + user.public_repos + "</p>"
    $(".information").append(informationStr);
    //User's Github URL
    var linkStr = "<p><a id = \"link\" style = \"color:blue\" href=\"" + user.html_url + "\" target=\"_blank\" >User's Github URL</a></p>"
    console.log(linkStr);
    $(".information").append(linkStr);

}

/**
 *
 * @param username: show error message that no such user can be found
 */
function noSuchUser(username) {
    //3. set the elements such that a suitable message is displayed
    console.log("in noSuchUser");
    //Reset all currently displayed information and show error message
    $(".avatar").empty();
    $(".information").empty();
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
