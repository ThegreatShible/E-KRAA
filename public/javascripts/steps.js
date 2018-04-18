var stepper = function(){
    var qst1 = $('#qst1');
    var qst2 = $('#qst2');
    var qst3 = $('#qst3');
    var qsts = [qst1,qst2,qst3] ;
    var nextBtn = $('#next');
    var prevBtn = $('#prev');
    var prog = $('.progress-bar');

    var i =1; 
    qst1.show();
    qst2.hide();
    qst3.hide();

var next = function(){
nextBtn.click(function() {

    qsts[i-1].hide();
    qsts[i].show();
    i++;
    var val = (i*150);
    prog.css("width",val);
  });

}
// var prev = function(){
//     prevBtn.click(function() {
//         qsts[i].hide();
//         qsts[i-1].show();
//         i--;
    
//       });
    
// }

return {
    // public functions
    init: function() {
       next();
       prev();
    }
};

}();

jQuery(document).ready(function() {
    stepper.init();
});