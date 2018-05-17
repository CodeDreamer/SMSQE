100 def proc set_csizes_QPTR (appsub_list,num%,x_csize%,y_csize%)
110 rem set the csizes for menu appsub windows
120 rem params:
130 rem   appsub_list
140 rem     the appsub list as returned by the RD_AWTA basic function  and/or
150 rem     the MK_AWL extension
160 rem   num%
170 rem     the appsub number, starting at 1 for the first appsub wdw
180 rem   x_csize%
190 rem      the x csize (0 ... 3), same as for the CSIZE command
200 rem   y_csize%
210 rem      the y csize (0 or 1), same as for the CSIZE command
220 rem
230 rem   If there is any error in the parameters, this jusy gives up silently
240 rem   without generating an error
250 rem
260 local appsub_nbr%,appsub
270   appsub_nbr%=peek_w(appsub_list)               : rem nbr of appsub wdws
280   if appsub_nbr% < num% or num% < 1: return     : rem wrong appsub number
290   appsub=peek_l(appsub_list+2+4*(num%-1))       : rem get poiner to appsub wdw
300   if x_csize% > -1 and x_csize% < 4
310       poke appsub+38,x_csize%                   : rem set size
320   endif
330   if y_csize% > -1 and y_csize% < 2
340       poke appsub+39,y_csize%                   : rem set size
350   endif
360 end def set_csizes_QPTR
370 :
380 def proc set_csizes_EASYPTR (channel%,num%,x_csize%,y_csize%)
390 rem set the csizes for menu appsub windows
400 rem params:
410 rem   channel%
420 rem     the channel number, which must be the same on as used for MAWDRAW
430 rem     or MAWSETUP. THIS DOES NOT DEFAULT TO ANYTHING, you MUST
440 rem     supply this parameter correctly.
450 rem   num%
460 rem     the appsub number, starting at 1 for the first appsub wdw
470 rem   x_csize%
480 rem      the x csize (0 ... 3), same as for the CSIZE command
490 rem   y_csize%
500 rem      the y csize (0 or 1), same as for the CSIZE command
510 rem
520 rem   If there is any error in the parameters, this jusy gives up silently
530 rem   without generating an error       S
540 rem
550 local appsub_nbr%,appsub
560   appsub= MWDEF(#channel%)                      : rem the working definition
570   appsub_nbr%=peek_w(appsub+110)                : rem nbr of appsub wdws
580   if appsub_nbr% < num% or num% < 1: return     : rem wrong appsub number
590   appsub=peek_l(appsub+112)                     : rem point to appsub list
600   appsub=peek_l(appsub+4*(num%-1))              : rem get poiner to appsub wdw
610   if x_csize% > -1 and x_csize% < 4
620       poke appsub+38,x_csize%                  : rem set size
630   endif
640   if y_csize% > -1 and y_csize% < 2
650       poke appsub+39,y_csize%                  : rem set size
660   endif
670 end def set_csizes_EASYPTR
680 :
