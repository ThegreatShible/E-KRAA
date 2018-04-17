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
    return {
        // public functions
        init: function() {
            demo1();
            demo2();
        }
    };
}();

jQuery(document).ready(function() {
    FormRepeater.init();
});