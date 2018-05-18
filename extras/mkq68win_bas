100 make_q68_win "Derek"
110 :
120 DEFine PROCedure make_q68_win (extn$)
130 LOCal mblen,c%,fleng,mdir$,orig_file$,drive%
140   drive%=8                              : REMark drive to which the OS win will be attached
150   orig_file$=WIN_DRIVE$(drive%)         : REMark file currently use by that
160   mdir$=NFA_USE$(1)                     : REMark wll be put into nfa1_
170 :
180   WIN_DRIVE drive%,""
190   COPY_O "nfa1_1MB.win","nfa1_Q68_SMSQ.WIN" : REMark make copy of empty template file, set it as winX
200   WIN_DRIVE drive%,mdir$&"Q68_SMSQ.WIN" : REMark use as win drive x
210 :                                       : REMark copy Q68 smqe file to it
220   IF extn$<>"":COPY_O "dev1_progs_MenuConf_INF_"&extn$,"dev1_progs_MenuConf_INF"
230   EW menuconfig;"\q\uDEV8_smsq_q68_QL_RAM.BIN3" : REMark set config
240   COPY_O "DEV8_smsq_q68_QL_RAM.BIN","win"&drive%&"_Q68_SMSQ"
250   MAKE_DIR "win"&drive%&"_config"
260   COPY DEV1_booty_Menu_rext_8e04,"win"&drive%&"_config_menu_rext_english"
270   COPY DEV1_progs_MenuConfig,"win"&drive%&"_config_MenuConfig"
280   COPY DEV1_q68_boot,"win"&drive%&"_boot"
290 :
300   WIN_DRIVE drive%,""                   : REMark win drive X no longer exists
310   WIN_DRIVE drive%,orig_file$           : REMark reset orig file to it
320   DELETE "nfa1_Q68_SMSQ.WIN.zip"
330   DELETE "nfa1_Q68_SMSQ.WIN.zip"&extn$
340   IF extn$<>""
350     COPY_O "dev1_progs_MenuConf_INF_mine","dev1_progs_MenuConf_INF"
360   END IF
370 :
380   REMark PRINT "done, win file copied & zipped for "& extn$
390 :
400 END DEFine make_q68_win
410 :
420 DEFine PROCedure sa
430   SAVE_O dev8_extras_mkq68win_bas
440 END DEFine sa
