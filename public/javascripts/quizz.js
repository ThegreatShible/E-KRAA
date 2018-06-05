var form = function () {
    var questions = {
        sessionID: String,
        userID: String,
        questionsAnswers: []
    };

    var question = {
        numQuestion: Number,
        answersNum: []
    };


    $("#formulaire").hide();
    var displayForm = function () {
        $("#start").click(function () {
            $("#formulaire").show();
        });
    };

    var submitForm = function () {
        $("#answers").click(function () {

            var fields = $("form").serializeArray();
            var nbrQuestions = $(".alert").length;
            console.log(fields);
            for (let index = 0; index < nbrQuestions; index++) {
                question.numQuestion = index + 1;
                answer = [];
                for (let i = 0; i < fields.length; i++) {
                    if (parseInt(fields[i].name) === index + 1) {
                        answer.push(fields[i].value);
                    }

                }
                question.answersNum = answer;
            }
            questions.questionsAnswers.push(question);
            console.log(questions);

            //add sessionID & userID
            var res = JSON.stringify(questions);

            //send data with AJAX 
            $.ajax({
                type: "POST",
                data: res,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                url: "http://localhost:9000/book/question/add",
                success: function (msg) {
                    $('.answer').html(msg);
                }
            });
        });
    };

    return {
        init: function () {
            displayForm();
            submitForm();
        }
    };

}();
//== Initialization
jQuery(document).ready(function () {
    form.init();
});