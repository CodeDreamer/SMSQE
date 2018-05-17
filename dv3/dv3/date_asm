; DV3 Date routine            V3.00           1992 Tony Tebby

        section dv3

        xdef    dv3_date

        include 'dev8_keys_qdos_sms'
;+++
; DV3 date
;
;       d1  r   (long) date
;
;       status return arbitrary
;---
dv3_date
ddt.reg reg    d2/a0
        movem.l ddt.reg,-(sp)

        moveq   #sms.rrtc,d0
        trap    #do.sms2

        movem.l (sp)+,ddt.reg
        rts
        end
