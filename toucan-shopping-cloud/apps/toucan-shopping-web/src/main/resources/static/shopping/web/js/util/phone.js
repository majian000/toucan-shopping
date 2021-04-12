
function validPhone(phone)
{
    var myreg=myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/;
    if(!myreg.test(phone)) {
        return false;
    }
    return true;
}