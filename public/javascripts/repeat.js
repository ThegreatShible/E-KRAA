var FormRepeater = function() {

    //== Private functions
    var demo1 = function() {
        $('#m_repeater_1').repeater({            
            initEmpty: false,
           
            defaultValues: {
                'text-input': 'foo'
            },  
            show: function () {
                $(this).slideDown();
            },

            hide: function (deleteElement) {                
                $(this).slideUp(deleteElement);                 
            }   
        });
    }
    var demo2 = function() {
        $('#m_repeater_2').repeater({            
            initEmpty: false,
           
            defaultValues: {
                'text-input': 'foo'
            },
             
            show: function() {
                $(this).slideDown();                               
            },

            hide: function(deleteElement) {                 
                    $(this).slideUp(deleteElement);
                                               
            }      
        });
    }
    var demo3 = function() {
        $('#m_repeater_3').repeater({            
            initEmpty: false,
           
            defaultValues: {
                'text-input': 'foo'
            },

            show: function() {
                $(this).slideDown();                               
            },

            hide: function(deleteElement) {                 
                    $(this).slideUp(deleteElement);                           
            }      
        });
    }
    var demo4 = function(){
        $('#m_repeater_1').repeater({
           
            repeaters: [{
                selector: '#m_repeater_3'
            }]
        });
    }
    return {
        // public functions
        init: function() {
            // demo1();
            // demo2();
            // demo3();
            demo4();

        }
    };
}();

jQuery(document).ready(function() {
    FormRepeater.init();
});