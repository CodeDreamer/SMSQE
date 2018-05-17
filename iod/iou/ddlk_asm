; Device Driver Linkage Link In   V2.00    1985  Tony Tebby   QJUMP

        section iou

        xdef    iou_ddlk
        xdef    iou_svlk

        include 'dev8_keys_iod'
        include 'dev8_keys_qdos_sms'

;+++
; General directory device driver link in.
;
;       a3 c  p pointer to linkage block
;
;       status return standard
;---
iou_ddlk
idl.reg reg     a0/a1
        movem.l idl.reg,-(sp)

        lea     iod_iolk(a3),a0          ; link in io device
        moveq   #sms.lfsd,d0
        trap    #do.sms2

iou_svlk
        tst.l   iod_xiad(a3)             ; any external interrupt server?
        beq.s   idl_poll                 ; ... no
        lea     iod_xilk(a3),a0          ; ... yes, link it in
        moveq   #sms.lexi,d0
        trap    #do.sms2

idl_poll
        tst.l   iod_plad(a3)             ; any polling interrupt server?
        beq.s   idl_shed                 ; ... no
        lea     iod_pllk(a3),a0          ; ... yes, link it in
        moveq   #sms.lpol,d0
        trap    #do.sms2

idl_shed
        tst.l   iod_shad(a3)             ; any scheduler server?
        beq.s   idl_exit                 ; ... no
        lea     iod_shlk(a3),a0          ; ... yes, link it in
        moveq   #sms.lshd,d0
        trap    #do.sms2

idl_exit
        movem.l (sp)+,idl.reg
        moveq   #0,d0                    ; OK
        rts
        end
