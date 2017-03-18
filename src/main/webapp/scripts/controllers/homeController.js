app.controller('homeController', ['$rootScope', '$location', HomeController]);

function HomeController($rootScope, $location){

    var self = this;
    this.$rootScope = $rootScope;
    this.$location = $location;
    this.prevScrollY =  0;
    this.myInterval = 4000;
    this.myTestiInterval = 3000;
    this.showMenu = this.showDownload = false;
    this.scrollCount = 0;
    this.betrHalfAge = "Select your Choice";
    
    this.scrollClass = [".top-banner", ".better-half-search", ".nav-options", ".about", ".reviews", ".footer-div"];

    this.slides = [
    				{
    					image:"images/slider-first.png"
    				},
    				{
    					image:"images/slider-first.png"
    				},
    				{
    					image:"images/slider-first.png"
    				}
                  ];

    this.slidesTesti = [
            				{
            					image:"images/testi-user-img1.jpg",
            					name:"Lorem ipsum dolor",
            					review:"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."
            				},
            				{
            					image:"images/testi-user-img1.jpg",
            					name:"Lorem ipsum dolor",
            					review:"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."
            				
            				},
            				{
            					image:"images/testi-user-img1.jpg",
            					name:"Lorem ipsum dolor",
            					review:"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."    				
            				}
                  ];
    
    $( window ).scroll(function(event) {
        if(self.$rootScope.isHome){
          /*self.autoScroll(event);*/
        }
          self.minTopBanner(event);
    });
    this.$rootScope.$on('minimizeBanner', function(event, mEvent){
      //self.minTopBanner(event);
    });
    this.$rootScope.$broadcast("homeBroadCast", true);
    this.$rootScope.$broadcast("profileBroadCast", false);
    
    
    $(window).on('scroll resize', this.check_if_in_view);

    $(window).trigger('scroll');

}

HomeController.prototype = {
    check_if_in_view : function(){
      var window_height = $(window).height();
      var window_top_position = $(window).scrollTop();
      var window_bottom_position = (window_top_position + window_height);
      var animation_elements = $('.animation-element');
      var currentElem = "";
      $.each(animation_elements, function() {
        var $element = $(this);
        var element_height = $element.outerHeight();
        var element_top_position = $element.offset().top;
        var element_bottom_position = (element_top_position + element_height);

        //check to see if this current container is within viewport
        if ((element_bottom_position >= window_top_position) &&
            (element_top_position <= window_bottom_position)) {
         setTimeout(function() {
           $element.addClass('in-view'); 
         });
          //$element.removeClass('out-view');
        } else {
          $element.removeClass('in-view');
         // $element.addClass('out-view');
        }
      }); 
    },
    minTopBanner : function(event){
      var scrollDiff = (event.originalEvent)? event.originalEvent.currentTarget.scrollY : 0; 
      $(".top-banner-view").addClass("animate-height");
      $(".top-banner").addClass("animate-height");
      if(scrollDiff>0){        
        $(".top-banner").addClass("min-banner-top-banner");
        $(".logo img").addClass("min-banner-logo");
        $(".app-download").addClass("min-banner-app-download");
        $(".top-banner-menu").addClass("min-top-banner-menu");
        $(".profile-nav").css("margin-top","56px");        
        $(".profile-nav").css("height","25px");      
        $(".profile-options span").css("padding","3px");
        $(".profile-options span img").css("width", "28px");
        $(".search-member").css("margin-top","12px");
      }else{
        $(".top-banner").removeClass("min-banner-top-banner");
        $(".logo img").removeClass("min-banner-logo");
        $(".app-download").removeClass("min-banner-app-download");
        $(".top-banner-menu").removeClass("min-top-banner-menu"); 
        $(".profile-nav").css("margin-top","80px");        
        $(".profile-nav").css("height","40px");          
        $(".profile-options span").css("padding","10px");
        $(".profile-options span img").css("width", "45px");
        $(".search-member").css("margin-top","28px");
      }
      this.prevScrollY = (event.originalEvent)? event.originalEvent.currentTarget.scrollY : this.prevScrollY;
    },
    autoScroll : function(event){
      var self = this,
       scrollDiff = (event.originalEvent)? (this.prevScrollY - event.originalEvent.currentTarget.scrollY) : 0,
       isUserScroll = false;

      if(scrollDiff === 100 || scrollDiff === -100){ 
        isUserScroll = true;
      } 

      if(event.originalEvent && this.prevScrollY < event.originalEvent.currentTarget.scrollY && isUserScroll){ 
      this.scrollCount = (this.prevScrollY < event.originalEvent.currentTarget.scrollYcrollCount < this.scrollClass.length-1)? this.scrollCount+1 : this.scrollClass.length-1;
      }
      else if(isUserScroll){ 
      this.scrollCount = (this.scrollCount>0)? this.scrollCount-1 : 0;
      }

      if(isUserScroll){
      $("html,body").animate({'scrollTop':$(""+self.scrollClass[self.scrollCount]).position().top},1000);
      }   

      this.prevScrollY = (event.originalEvent)? event.originalEvent.currentTarget.scrollY : this.prevScrollY;
   },
   addClass : function(element, animationClass){
       //$("."+element).addClass(animationClass);  
   },
   removeClass : function(element,animationClass){
      //$("."+element).removeClass(animationClass); 
   }

};