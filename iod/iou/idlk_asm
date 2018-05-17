; Device Driver Linkage Link In   V2.00    1985  Tony Tebby   QJUMP

        section iou

        xdef    iou_idlk

        xref    iou_svlk

        include 'dev8_keys_iod'
        include 'dev8_keys_qdos_sms'

;+++
; General IO device driver link in.
;
;       a3 c  p pointer to linkage block
;
;       status return standard
;---
iou_idlk
idl.reg reg     a0/a1
        movem.l idl.reg,-(sp)

        lea     iod_iolk(a3),a0          ; link in io device
        tst.l   4(a0)                    ; any?
        beq.s   iou_svlk                 ; ... no, try the others
        moveq   #sms.liod,d0
        trap    #do.sms2
        bra.s   iou_svlk                 ; link in any other servers
        end
