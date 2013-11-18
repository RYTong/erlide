<?xml version="1.0" encoding="UTF-8" ?>
<content>
    <head>
        <style>
        .title_div{left:0px;top:0px;width:320px;height:51px;background-image:url(title_bg.png);}
        .label_title{left:123px;top:11px;font-size:19px;font-weight:bold;color:#FFFFFF;}
        .button_main{left:260px;top:6px;width:54px;height:32px;background-image:url(main_but.png);}
        .button_back{left:8px;top:6px;width:54px;height:32px;background-image:url(back_but.png);}
        .acc_div{left:0px;top:51px;width:320px;}

        </style>
        <script type="text/x-lua" src="RYTL.lua"></script>
        <script type="text/x-lua">
            <![CDATA[

               ryt = RYTL:new{};

               function main_page()
                  local result = history:get(-1);
                  location:replace(result);
               end;

               function back_fun()
                  local result = history:get(-1);
                  location:replace(result);
               end;

               --[[Next Page]]--
               function common_req()
                  trancode="";
                  invoke_trancode(channelId, trancode, {}, callback_channel, {});
               end;

            ]]>
        </script>
    </head>

    <body>

      <!--Title-->
      <div class="title_div" border="0">
        <label class="label_title">标题</label>
        <input type="button" class="button_back" name="back_but" onclick="back_fun()"/>
        <input type="button" class="button_main" name="main_but" onclick="main_page()"/>
      </div>
      <div class="acc_div" name="div_acc" border="0">
        <label>此处填页面内容．</label>
      </div>
    </body>
</content>
