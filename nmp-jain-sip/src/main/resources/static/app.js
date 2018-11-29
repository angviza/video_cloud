var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var token = $("#token").val();
    var wsId = $("#wsId").val();
    var socket = new SockJS('/sipWebSocket?token='+token + "&wsId=" +wsId);
    stompClient = Stomp.over(socket);
    stompClient.connect({token: token}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        var address = $("#address").val();
        stompClient.subscribe(address, function (data) {
            //var obj = JSON.parse(data.body);
            //showGreeting(obj);
            showGreeting(JSON.stringify(data.body));
        });

        stompClient.subscribe("/user/single/message", function (data) {
            $("#pushRes").text(JSON.stringify(data.body));
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendData() {
    var wsId = $("#wsId").val();
    var address = $("#pushAddress").val();
    console.info("==========="+$("#data").val());
    stompClient.send(address, {wsId: wsId}, $("#data").val());
}


function showGreeting(message) {
    $("#greetings").append("<tr><td title='" + message + "'>" + message.substring(0, 100) + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendData();
    });
});

