(function ($) {

    "use strict";
    
        //Dropdown Button
        $('.dropdown').hover(function () {
            if ($(window).innerWidth() >= 992) {
                $(this).children('ul').stop(true, false, true).slideToggle(300);
            }
        });
    
        $('.dropdown-toggle').on('click', function () {
            if ($(window).innerWidth() <= 992) {
                $(this).next('ul.dropdown-menu').slideToggle(300);
            }
        });
    
    
        // For Banner Slider Js
        $(".banner-slider").slick({
            autoplay: false,
            autoplaySpreed: 2000,
            arrows: true,
            fade: true,
            prevArrow: '<i class="fas fa-caret-left"></i>',
            nextArrow: '<i class="fas fa-caret-right"></i>',
        });
    
        // For Counter Js
        $(".counter").counterUp({
            delay: 20,
            time: 2000,
        });
    
        // For Testimonial Slider Js
        $(".testimonial-slider").slick({
            autoplay: true,
            autoplaySpreed: 2000,
            arrows: true,
            fade: true,
            prevArrow: '<i class="fas fa-caret-left"></i>',
            nextArrow: '<i class="fas fa-caret-right"></i>',
        });
    
        $('.img').magnificPopup({
            type:'image',
            gallery: {
                enabled: true,
                navigateByImgClick: true,
            },
        });
    
    
    /* ==========================================================================
       When document is loaded, do
       ========================================================================== */

    $(window).on('load', function () {

        //Hide Loading Box (Preloader)
        function handlePreloader() {
            if ($('.preloader').length) {
                $('.preloader').delay(200).fadeOut(500);
            }
        }
        handlePreloader();
        
    });
    
    
    })(window.jQuery);