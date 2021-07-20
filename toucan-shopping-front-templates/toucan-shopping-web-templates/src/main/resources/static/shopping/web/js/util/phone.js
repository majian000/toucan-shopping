
function validPhone(phone)
{
    var myreg=/^1[34578]\d{9}$/;
    return myreg.test(phone);
}