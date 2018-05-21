var createQuizz = function () {

    var qsts = [{
            "index": "number",
            "qst": "string"
        }],
        correct = [{
            "index": "number",
            "corIndex": "number",
            "value": "boolean"
        }],
        answer = [{
            "index": "number",
            "corIndex": "number",
            "ans": "string"
        }];

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
    var questions = Array[question];
    var getAttrs = function () {
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
                // while (parseInt(fields[index].name.charAt(1) <i)) {
                //     if (fields[index].name.replace(/[^a-z]/gi, '') == "question") {
                //         question.index = i ;
                //         question.question= fields[index].value ;
                //     };
                //     if (fields[index]) {

                //     }
                // }

            }
            console.log(qsts);
            console.log(correct);
            console.log(answer);
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