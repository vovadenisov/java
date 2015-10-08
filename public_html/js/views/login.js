define([
    'backbone',
    'tmpl/login',
    'models/user',
    'utils/validation'
], function(
    Backbone,
    login,
    User,
    Validator
){
    var errorMessageElement = '.b-main-login-form__error-message';
    var inputClassPrefix = '.b-main-login-form__input_type_';
  

    var validator = new Validator(['name', 'password'],
                                  inputClassPrefix, errorMessageElement);

    var LoginView = Backbone.View.extend({

        template: login(),

        render: function () {
            this.$el.html(this.template);
        },

        events: {
            "click .b-main-login-form__submit-login-button": "submitLogin",
            "click a": "hide"
        },


        show: function () {
            this.render();            
        },

        hide: function () {
            this.$el.empty();
        },
     
        submitLogin: function(event) {
            event.preventDefault();
            var validatedData = validator.validateAuthForm();
            if (validatedData.isValid) {
                $(errorMessageElement).empty();
                var newUser = new User(validatedData);
                var that = this;
                newUser.logIn({
                    error: function(response) {
                        validator.parseServerResponse(response); 
                    },
                    success: function(response) {
                        validator.parseServerResponse(response);  
                        //that.hide();            
                        //Backbone.history.navigate('main', true);
                    } 
                });
            }
            else {
                validator.showErrorMessage(validatedData.errorMessage, validatedData.firstIncorrectInput);
            }
        }

    });

    return new LoginView({el: $('.b-inner-main-window')});
});
