(function ($) {
	var arrFile = [];
	$.Tupload = {
		fileNum: 0,
		uploadFile: [],
		options: null,
		imgsBase64: [],
		fileSize: 0,
		intiImgUrl: [],
		init: function (defaults) {
			this.options = defaults;
			this.fileNum = defaults.fileNum;
			this.fileSize = defaults.fileSize;
			this.intiImgUrl = defaults.intiImgUrl || [];
			this.createHtml(defaults);
			$(".uploading-img li").mouseenter(function () {
				$(this).find(".uploading-tip").stop().animate({ height: '25px' }, 200);
			});
			$(".uploading-img li").mouseleave(function () {
				$(this).find(".uploading-tip").stop().animate({ height: '0' }, 200);
			});
			this.photoImg();
		},
		photoImg: function () {
			var photoImgH = $('.uploading-imgBg').height();
			var ImgH = $('.uploading-imgBg img').height();
			if (ImgH > photoImgH) {
				$('.uploading-imgBg img').addClass('cur');
			} else {
				$('.uploading-imgBg img').removeClass('cur');
			}
		},
		createHtml: function (defaults) {

			var fileNum = defaults.fileNum, title = defaults.title, divId = defaults.divId, accept = defaults.accept, intiImgUrl = defaults.intiImgUrl;
			var html = "";
			var hasImgNum = 0;
			if (fileNum < 0 && fileNum == null) {
				fileNum = 5;
			}
			if (intiImgUrl instanceof Array) {
				hasImgNum = intiImgUrl.length
			}
			html += '<div class="uploading-img">';
			html += '<p>' + title == '' ? '' : title + '</p>';
			html += `<input type="hidden" id="fileNum" value="${hasImgNum}">`;
			html += '<ul class="picView-magnify-list">';

			var src = '';
			for (var i = 0; i < fileNum; i++) {
				var style = 'display: none';
				if (intiImgUrl instanceof Array && intiImgUrl.length > i) {
					src = intiImgUrl[i];
					style = '';
					this.imgsBase64[i] = src;

				} else {
					src = '/static/lib/tupload/images/imgadd.png';
				}
				// console.log(src)
				html += '<li  data-toggle="tooltip" data-placement="top" title="点击图片预览">';
				html += `<div id='imgBg_div${i}' class="uploading-imgBg" data-magnify="gallery" data-src="${src}" data-caption="图片预览">`;
				html += '<img id="img_src' + i + `" class="upload_image" src="${src}"/>`;
				html += '</div>';
				// html += '<p id="uploadProgress_'+i+'" class="uploadProgress"></p>';
				// html += '<p id="uploadTure_'+i+'" class="uploadTrue"></p>';
				html += '<div id="uploading-tip' + i + `" class="uploading-tip" style="${style}">`;
				// html += 	'<span class="onLandR" data="left,'+i+'" ><</span>';
				html +=  '<i class="onDelPic" data="' + i + '">删除</i></div>';
				html += '</li>';

			}
			html += '</ul>';
			html += '<div class="uploading-imgInput">';
			html += '<input readonly="readonly" id="fileText" type="text" class="imgInput-file" placeholder="最多选择' + fileNum + '张图片"/>';
			html += '<div class="andArea">';
			html += '<div class="filePicker">浏览图片</div>';
			html += '<input id="fileImage" name="previewPhotoFiles" type="file" multiple accept=' + accept + '>';
			html += '</div>';
			html += '</div>';
			html += '</div>';
			$("#" + divId).html(html);
			$("#fileImage").change(function () {
				var num = parseInt($("#fileNum").val()) + parseInt(this.files.length);
				if (num < $.Tupload.fileNum + 1) {
					$("#fileNum").val(num);
					// $("#fileText").val("选中"+num+"张文件");
					for (var i = 0; i < num; i++) {
						for (var j = 0; j < num; j++) {
							if ($("#img_src" + j).attr("src") == "/static/lib/tupload/images/imgadd.png") {
								if (this.files.length - 1 < i) {
									break;
								} else {
									var size = $.Tupload.fileSize;
									// console.log(size)
									// console.log(this.files[i].size)
									if (size !== 0) {
										if (this.files[i].size > size) {
											break;
										}
									}
									$.Tupload.imgLoad(i, this.files[i]);
									break;
								}
							}
						}
					}
				} else {
					alert("最多上传" + $.Tupload.fileNum + "张");
				}
			});
			$(".onDelPic").on('click', function () {
				$.Tupload.onDelete($(this).attr("data"));
			});
			$(".upload_image").on('click', function () {
				$.Tupload.setPreView();
			})

		},
		imgLoad: function (i, file) {
			var self = this;
			var r = new FileReader();
			r.readAsDataURL(file);
			$(r).load(function () {
				while (true) {
					if ($("#img_src" + i).attr("src") != "/static/lib/tupload/images/imgadd.png") {
						i++;
					} else {
						break;
					}
				}
				arrFile[i] = file;
				$("#imgBg_div" + i).attr("data-src", this.result);
				$("#img_src" + i).attr("src", this.result);
				$("#uploading-tip" + i).show();
				self.imgsBase64[i] = this.result;

				$.Tupload.setPhotoImg();
			});
		},
		setPhotoImg: function () {
			var divH = $('.uploading-imgBg').height(); //获取容器高度
			var img = $('.uploading-imgBg img');
			for (var i = 0; i < img.length; i++) {
				var H = $('.uploading-imgBg img').eq(i).height();
				if (H > divH) {
					//当图片高度大于容器高度时
					//$('.uploading-imgBg img:eq('+i+')').css('margin-top',-(H-divH)/2+"px");
				} else {
					// $('.uploading-imgBg img:eq('+i+')').css('margin-top',(divH-H)/2+"px");
				}
			}
		},
		setPreView: function() {
			$('[data-magnify]').Magnify({
				Toolbar: [
					'prev',
					'next',
					'rotateLeft',
					'rotateRight',
					'zoomIn',
					'actualSize',
					'zoomOut'
				],
				keyboard:true,
				draggable:true,
				movable:true,
				modalSize:[800,600],
				beforeOpen:function (obj,data) {
					console.log('beforeOpen')
				},
				opened:function (obj,data) {
					console.log('opened');
				},
				beforeClose:function (obj,data) {
					console.log('beforeClose')
				},
				closed:function (obj,data) {
					console.log('closed')
				},
				beforeChange:function (obj,data) {
					console.log('beforeChange')
				},
				changed:function (obj,data) {
					console.log('changed')
				}
			});
		},

		onSuccess: function (data, i) {
			return $.Tupload.options.onSuccess(data, i);
		},
		onDelete: function (i) {
			$.Tupload.options.onDelete(i);
			$("#uploadTure_" + i).hide();
			$("#img_src" + i).attr("value", "");
			$("#img_src" + i).attr('name', "");
			var num = parseInt($("#fileNum").val()) - 1;
			$("#fileNum").val(num);
			// $("#fileText").val("选中"+num+"个文件");
			arrFile[i] = '';
			$("#imgBg_div" + i).attr("data-src", "/static/lib/tupload/images/imgadd.png");
			$("#img_src" + i).attr("src", "/static/lib/tupload/images/imgadd.png");
			$("#uploading-tip" + i).hide();
			this.imgsBase64[i] = '';

			$.Tupload.setPhotoImg();
			$("#fileImage").val('')
		}

	}
})(jQuery);

