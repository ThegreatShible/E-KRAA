// var createQuizz = function () {

//     var qsts = [],
//         correct = [],
//         answer = [],
//         weight = [];

//     var question = {
//         "index": "string",
//         "question": "string",
//         "answers": [{
//             "answer": "string",
//             "right": "boolean",
//             "numAnswer": "number"
//         }],
//         "multi": "boolean",
//         "weight": "number"
//     };

//     var getAttrs = function () {
//         var questions = [];
//         $("#submit").click(function () {

//             var fields = $("form").serializeArray();
//             console.log(fields);

//             for (let index = 0; index < fields.length; index++) {
//                 if (fields[index].name.replace(/[^a-z]/gi, '') == "question") {
//                     qsts.push({
//                         "index": parseInt(fields[index].name.charAt(1)),
//                         "qst": fields[index].value
//                     });
//                 };
//                 if (fields[index].name.replace(/[^a-z]/gi, '') == "weight") {
//                     weight.push({
//                         "index": parseInt(fields[index].name.charAt(1)),
//                         "wei": fields[index].value
//                     });
//                 };
//                 if (fields[index].name.replace(/[^a-z]/gi, '') == "correct") {
//                     correct.push({
//                         "index": parseInt(fields[index].name.charAt(1)),
//                         "corIndex": parseInt(fields[index].name.charAt(6)),
//                         "value": true
//                     });
//                 };
//                 if (fields[index].name.replace(/[^a-z]/gi, '') == "seggest") {
//                     answer.push({
//                         "index": parseInt(fields[index].name.charAt(1)),
//                         "corIndex": parseInt(fields[index].name.charAt(6)),
//                         "ans": fields[index].value
//                     });
//                 };
//             }
//             for (let i = 0; i < qsts.length; i++) {
//                 question.index = qsts[i].index;
//                 question.question = qsts[i].qst;
//                 question.weight = weight[i].wei;
//                 if (correct.length > 1) {
//                     question.multi = true;
//                 } else {
//                     question.multi = false;
//                 }

//                 function filterByID(item) {
//                     if (item.index === qsts[i].index) {
//                         return true;
//                     }
//                     return false;
//                 };
//                 var arrAns = answer.filter(filterByID);
//                 console.log(arrAns);
//                 question.answers = [];
//                 arrAns.forEach(ans => {

//                     var tempanswer = {};
//                     tempanswer.answer = ans.ans;
//                     tempanswer.numAnswer = ans.corIndex + 1;
//                     if (correct.find(o => o.corIndex === ans.corIndex)) {
//                         tempanswer.right = true;
//                     } else {
//                         tempanswer.right = false;
//                     }
//                     question.answers.push(tempanswer);
//                 });

//                 questions.push(JSON.stringify(question));
//                 var bookid = $("#bookID").text();


//             }
//             // send data with ajax
//             $.ajax({
//                 type: "POST",
//                 data: {
//                     questions: questions
//                 },
//                 url: "../html/createQuizz.html",
//                 success: function (msg) {
//                     $('.answer').html(msg);
//                 }
//             });
//             console.log(questions);
//             console.log
//         });
//     }


//     return {
//         init: function () {
//             getAttrs();
//         }
//     };
// }();

// //== Initialization
// jQuery(document).ready(function () {
//     createQuizz.init();
// });

var createQuizz = function () {

<<<<<<< HEAD
    var qsts = [],
        correct = [],
        answer = [],
        weight = [];

    var question = {
        "questionNum": "string",
        "question": "string",
        "answers": [{
            "answer": "string",
            "right": "boolean",
            "numAnswer": "number"
        }],
        "multiple": "boolean",
        "weight": "number"
    };

    var getAttrs = function () {
        var questions = [];
        var quizz = new Object();
=======
    var qsts = [], correct = [], answer = [], weight = [];


    var quizz = {
        "bookID": "number",
        "questions": []
    };


    var getAttrs = function () {
        var questions = [];

>>>>>>> upstream/master
        $("#submit").click(function () {

            var fields = $("form").serializeArray();

            for (let index = 0; index < fields.length; index++) {
                if (fields[index].name.replace(/[^a-z]/gi, '') == "question") {
                    qsts.push({
                        "index": parseInt(fields[index].name.charAt(1)),
                        "qst": fields[index].value
                    });
<<<<<<< HEAD
                };;
=======
                }
                ;
>>>>>>> upstream/master
                if (fields[index].name.replace(/[^a-z]/gi, '') == "weight") {
                    weight.push({
                        "index": parseInt(fields[index].name.charAt(1)),
                        "wei": fields[index].value
                    });
<<<<<<< HEAD
                };
=======
                }
                ;
>>>>>>> upstream/master
                if (fields[index].name.replace(/[^a-z]/gi, '') == "correct") {
                    console.log(fields[index].name)
                    correct.push({
                        "index": parseInt(fields[index].name.charAt(1)),
                        "corIndex": parseInt(fields[index].name.charAt(6)),
                        "value": true
                    });
<<<<<<< HEAD
                };
=======
                }
                ;
>>>>>>> upstream/master
                if (fields[index].name.replace(/[^a-z]/gi, '') == "suggest") {
                    answer.push({
                        "index": parseInt(fields[index].name.charAt(1)),
                        "corIndex": parseInt(fields[index].name.charAt(6)),
                        "ans": fields[index].value
                    });
                }
                ;
            }
<<<<<<< HEAD
            console.log(answer);
            for (let i = 0; i < qsts.length; i++) {
=======
            /*for ( i = 0; i < qsts.length; i++) {
>>>>>>> upstream/master
                question.questionNum = qsts[i].index;
                question.question = qsts[i].qst;
                question.weight = weight[i].wei;
                if (correct.length > 1) {
                    question.multiple = true;
                } else {
                    question.multiple = false;
<<<<<<< HEAD
                };
=======
                }
>>>>>>> upstream/master

                function filterByID(item) {
                    if (item.index === qsts[i].index) {
                        return true;
                    }
                    return false;
                };
                var arrAns = answer.filter(filterByID);
                question.answers = [];
                arrAns.forEach(ans => {

                    var tempanswer = {};
<<<<<<< HEAD
                    tempanswer.answer = ans.ans;
                    tempanswer.numAnswer = ans.corIndex + 1;
                    if (correct.find(o => o.corIndex === ans.corIndex)) {
                        tempanswer.right = true;
                    } else {
                        tempanswer.right = false;
                    }
                    question.answers.push(tempanswer);
                });
                console.log(question);
                questions.push(JSON.stringify(question));
                
            }
            console.log(questions);
            quizz.bookID = $("#bookID").text();
            quizz.questions = questions;
            var quizz2 = new Object();
            quizz2 = JSON.stringify(quizz);
=======
                tempanswer.answer = ans.ans;
                tempanswer.numAnswer = ans.corIndex + 1;
                if (correct.find(o => o.corIndex === ans.corIndex))
                {
                    tempanswer.right = true;
                }
            else
                {
                    tempanswer.right = false;
                }
                question.answers.push(tempanswer);

            });
                questions.push(question);*/


            for (i = 0; i < qsts.length; i++) {
                var question = Object();
                question.questionNum = i;
                question.question = qsts[i].qst;
                question.weight = weight[i].wei;
                if (correct.length > 1) {
                    question.multiple = true;
                } else {
                    question.multiple = false;
                }

                function filterByID(item) {
                    if (item.index === qsts[i].index) {
                        return true;
                    }
                    return false;
                };
                var arrAns = answer.filter(filterByID);
                question.answers = [];
                arrAns.forEach(function (ans, j) {

                    var tempanswer = {};
                    tempanswer.answer = ans.ans;
                    tempanswer.numAnswer = j + 1;
                    if (correct.find(o = > o.corIndex === ans.corIndex && o.index === ans.index))
                    {
                        tempanswer.right = true;
                    }
                else
                    {
                        tempanswer.right = false;
                    }
                    question.answers.push(tempanswer);

                });
                quizz.questions.push(question);

            }
            quizz.bookID = Number($("#bookID").text());
            quizz2 = JSON.stringify(quizz);
            console.log(quizz2)
>>>>>>> upstream/master

            // send data with ajax
            $.ajax({
                type: "POST",
                data: quizz2,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                url: "http://localhost:9000/book/question/add",
<<<<<<< HEAD
                success: function (msg) {
                    $('.answer').html(msg);
                }
            });
            console.log(quizz);
            console.log(qsts);
=======
                success: function (response) {
                    window.location.replace('/auth/LoginPage');
                    //window.location.href = "localhost:9000/auth/loginPage";
                    //$('.answer').html(msg);
                },

            });
>>>>>>> upstream/master
        });
    }


    return {
        init: function () {
            getAttrs();
        }
    };
}();

//== Initialization
jQuery(document).ready(function () {
    createQuizz.init();
});