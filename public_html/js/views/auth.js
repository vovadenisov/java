define([
    'backbone',
    'tmpl/auth',
    'models/user',
    'utils/validation'
], function(
    Backbone,
    auth,
    User,
    Validator
){
    var errorMessageElement = '.b-main-signup-form__error-message';
    var inputClassPrefix = '.b-main-signup-form__input_type_';
  
    var checkPasswordsEquality = function(objForValidation) {
        if (objForValidation.password == objForValidation['repeated-password']) {
            delete objForValidation['repeated-password'];
            return objForValidation;
        }
        return {
            errorMessage: 'Пароли не совпадают',
            firstIncorrectInput: this.inputClassPrefix + 'password'
            };
    }


    var validator = new Validator(['name', 'email', 'password', 'repeated-password'],
                                  inputClassPrefix, errorMessageElement, checkPasswordsEquality);

    var AuthView = Backbone.View.extend({

        template: auth(),

        render: function () {
            this.$el.html(this.template);
        },

        events: {
            "click .b-main-signup-form__submit-login-button": "submitAuth",
            "click a": "hide"
        },


        show: function () {
            this.render();            
        },

        hide: function () {
            this.$el.empty();
        },
     
        submitAuth: function(event) {
            event.preventDefault();
            var that = this;
            console.log(that);
            var validatedData = validator.validateAuthForm();
            if (validatedData.isValid) {
                $(errorMessageElement).empty();
                var newUser = new User(validatedData);
                newUser.signUp({
                    error: function(model, response) {
                        validator.parseServerResponse(response); 
                    },
                    success: function(model, response) {
                        validator.parseServerResponse(response); 
                        //that.hide();
                        Backbone.history.navigate('main', true);
                    } 
                });
            }
            else {
                validator.showErrorMessage(validatedData.errorMessage, validatedData.firstIncorrectInput);
            }
        }
    });

    return new AuthView({el: $('.b-inner-main-window')});
});
