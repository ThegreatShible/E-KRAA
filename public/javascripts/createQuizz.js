var createQuizz = function () {

    var qsts = [],
        correct = [],
        answer = [];

    var question = {
        "index": "string",
        "question": "string",
        "answers": [{
            "answer": "string",
            "right": "boolean",
            "numAnswer": "number"
        }],
        "multi": "boolean",
        "weight": "number"
    };
    
    var getAttrs = function () {
        var questions = [];
        $("#submit").click(function () {

            var fields = $("form").serializeArray();
            console.log(fields);

            for (let index = 0; index < fields.length; index++) {
                if (fields[index].name.replace(/[^a-z]/gi, '') == "question") {
                    qsts.push({
                        "index": parseInt(fields[index].name.charAt(1)),
                        "qst": fields[index].value
                    });
                };
                if (fields[index].name.replace(/[^a-z]/gi, '') == "correct") {
                    correct.push({
                        "index": parseInt(fields[index].name.charAt(1)),
                        "corIndex": parseInt(fields[index].name.charAt(6)),
                        "value": true
                    });
                };
                if (fields[index].name.replace(/[^a-z]/gi, '') == "seggest") {
                    answer.push({
                        "index": parseInt(fields[index].name.charAt(1)),
                        "corIndex": parseInt(fields[index].name.charAt(6)),
                        "ans": fields[index].value
                    });
                };
            }
            for (let i = 0; i < qsts.length; i++) {
                question.index = qsts[i].index ;
                question.question = qsts[i].qst ;
                function filterByID(item) {
                    if (item.index ==qsts[i].index) {
                      return true;
                    } 
                    return false; 
                  } ; 
                var arrAns = answer.filter(filterByID);
                console.log(arrAns);
                arrAns.forEach(ans => {

                   var tempanswer = {};
                   tempanswer.answer = ans.ans;
                    tempanswer.numAnswer = ans.corIndex + 1 ;
                    if (correct.find(o => o.corIndex === ans.corIndex)) {
                        tempanswer.right = true;
                    }else{
                        tempanswer.right = false;
                    }
                   question.answers.push(tempanswer); 
                });
                
               questions.push(JSON.stringify(question)); 
            }
            console.log(questions);
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