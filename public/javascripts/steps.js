var stepper = function(){
    var qst1 = $('#qst1');
    var qst2 = $('#qst2');
    var qst3 = $('#qst3');
    
    qst1.show();
    qst2.hide();
    qst3.hide();

var next = function(){


}


return {
    // public functions
    init: function() {
       next();
    }
};

}();

jQuery(document).ready(function() {
    stepper.init();
});