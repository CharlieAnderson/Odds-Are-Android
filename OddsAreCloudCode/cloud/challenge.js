
Parse.Cloud.afterSave("Challenge", function(request) {
	Parse.Cloud.useMasterKey();
	var challenge = request.object;

    console.log(challenge.get("status"));
    updateLastUpdated(request);

    switch(challenge.get("status")) {
        case "challenge_submitted":
            console.log("setting up new challenge");
            setupNewChallenge(challenge, request);
            break;
        case "challenge_rejected":
            console.log("sending challenge rejected notification");
            challengeRejectedPushNotification(request);
            break;
        case "challengee_set_odds":
            console.log("sending challenger set odds notification");
            challengeeSetOddsPushNotification(request);
            break;
        case "waiting_for_result":
            console.log("challenger: " + challenge.challengerSeen);
            console.log("challengee: " + challenge.challengeeSeen);
            if(challenge.get("challengerSeen") == false && challenge.get("challengeeSeen") == false) {
                console.log("sending challengee results notification");
                challengerSetOddsPushNotification(request);
            }
            break;
        default:
            console.log("undefined status: " + challenge.get("status"));
            break;
    }
});

var setupNewChallenge = function(challenge, request) {
    console.log("sending new challenge notification");
        newChallengePushNotification(request);

        var challenger = request.object.get("challenger");
            challenger.fetch().then(function(fetchedChallenger) {
            console.log("fetched");
            var challenges = fetchedChallenger.get("challenges");
            challenges.push(challenge);
            fetchedChallenger.set("challenges", challenges);
            console.log("updated count: " + challenges.length);
            fetchedChallenger.save();
        });

        var challengee = request.object.get("challengee");
            challengee.fetch().then(function(fetchedChallengee) {
            console.log("fetched");
            var challenges = fetchedChallengee.get("challenges");
            challenges.push(challenge);
            fetchedChallengee.set("challenges", challenges);
            fetchedChallengee.save();
        });
}


var newChallengePushNotification = function(request) {
    Parse.Cloud.useMasterKey();
    var challengee = request.object.get("challengee");
    var installationQuery = new Parse.Query(Parse.Installation);
    installationQuery.equalTo("user", challengee);
    console.log("installation: " + installationQuery);

    var challenger = request.object.get("challenger"); //get challenger object
    challenger.fetch().then(function(fetchedChallenger){ //fetch the challenger object
            var challengerName = fetchedChallenger.get("name"); //get the challenger name
            payload = { //create push payload
                alert: challengerName + " has challenged you!",
                category: request.object.id,
                badge: "Increment",
                sound: "default",
                type: "nt"
            };

            Parse.Push.send({ //send push notification
                where: installationQuery,
                data: payload
                }).then(function() {  // Push succeed
                    console.log("Push Notification for new challenge successful.");
                }, function(error) {  // Push failed
                    console.log("Push Notification for new challenge failed.");
                    console.log(error);
        });
    });
};

var challengeRejectedPushNotification = function(request) {
    Parse.Cloud.useMasterKey();
    var challenger = request.object.get("challenger");
    var installationQuery = new Parse.Query(Parse.Installation);
    installationQuery.equalTo("user", challenger);

    var challengee = request.object.get("challengee");
    challengee.fetch().then(function(fetchedChallengee) {
        var challengeeName = fetchedChallengee.get("name");
        var payload = {
            alert: challengeeName + " has rejected your challenge.",
            category: request.object.id,
            sound: "default",
            type: "nt"
        };

        Parse.Push.send({ //send push notification
            where: installationQuery,
            data: payload
            }).then(function() {  // Push succeed
                console.log("Push Notification for new challenge successful.");
            }, function(error) {  // Push failed
                console.log("Push Notification for new challenge failed.");
                console.log(error);
        });
    });
};

var challengeeSetOddsPushNotification = function(request) {
    Parse.Cloud.useMasterKey();
    var challenger = request.object.get("challenger");
    var installationQuery = new Parse.Query(Parse.Installation);
    installationQuery.equalTo("user", challenger);

    var challengee = request.object.get("challengee");
    challengee.fetch().then(function(fetchedChallengee) {
        var challengeeName = fetchedChallengee.get("name");
        var payload = {
            alert: challengeeName + " has set their odds. It's your turn.",
            category: request.object.id, 
            badge: "Increment",
            sound: "default",
            type: "nt"
        };

        Parse.Push.send({ //send push notification
            where: installationQuery,
            data: payload
            }).then(function() {  // Push succeed
                console.log("Push Notification for new challenge successful.");
            }, function(error) {  // Push failed
                console.log("Push Notification for new challenge failed.");
                console.log(error);
        });
    });
};

var challengerSetOddsPushNotification = function(request) {
    Parse.Cloud.useMasterKey();
    var challengee = request.object.get("challengee");
    var installationQuery = new Parse.Query(Parse.Installation);
    installationQuery.equalTo("user", challengee);
    console.log("installation: " + installationQuery);

    var challenger = request.object.get("challenger"); //get challenger object
    challenger.fetch().then(function(fetchedChallenger){ //fetch the challenger object
            var challengerName = fetchedChallenger.get("name"); //get the challenger name
            payload = { //create push payload
                alert: challengerName + " has submitted their odds. You can now view the result!",
                category: request.object.get("objectId"),
                sound: "default",
                type: "nt"
            };

            Parse.Push.send({ //send push notification
                where: installationQuery,
                data: payload
                }).then(function() {  // Push succeed
                    console.log("Push Notification for new challenge successful.");
                }, function(error) {  // Push failed
                    console.log("Push Notification for new challenge failed.");
                    console.log(error);
        });
    });
};

var updateLastUpdated = function(request) {
    Parse.Cloud.useMasterKey();
    var challengee = request.object.get("challengee");
    var challenger = request.object.get("challenger");

    console.log("updating private data...");

    challenger.fetch().then(function(fetchedChallenger){
        var userPrivateData = fetchedChallenger.get("privateData");
        userPrivateData.fetch().then(function(upd){
            var date = new Date();
            upd.set("challengeLastUpdated", date);
            upd.save();
            console.log("updated challenger...");
        });
    });

    challengee.fetch().then(function(fetchedChallengee){
        var userPrivateData = fetchedChallengee.get("privateData");
        userPrivateData.fetch().then(function(upd){
            var date = new Date();
            upd.set("challengeLastUpdated", date);
            upd.save();
            console.log("updated challengee...");
        });
    });
}


Parse.Cloud.define("refreshOdds", function(request, response){
    Parse.Cloud.useMasterKey();
    var challenges = request.user.get("challenges");

    response.success(challenges.reverse());
    
});

Parse.Cloud.define("shouldRefresh", function(request, response) {
    Parse.Cloud.useMasterKey();
    var privateData = request.user.get("privateData");
    privateData.fetch().then(function(upd){
        var lastChecked = upd.get("lastChecked");
        var lastUpdated = upd.get("challengeLastUpdated");
        console.log("last checked " + lastChecked);
        console.log("last updated " + lastUpdated);
        if(lastUpdated >= lastChecked){
            response.success(true);
        } else {
            response.success(false);
        }
    });
});

