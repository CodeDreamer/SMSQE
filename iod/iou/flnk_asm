; IO Device find device linkage   V2.02     1986  Tony Tebby  QJUMP

        section exten

        xdef    iou_flnk
        xdef    iou_ipar

        include dev8_keys_err
        include dev8_keys_iod
        include dev8_keys_sys
        include dev8_keys_qdos_sms
;+++
; This routine finds the device driver linkage using the device name.
;     
;       d1   s
;       d2   s
;       d7 c  p three character + '0' device name
;       a0  r   pointer to iod_iolk
;
;       status return 0 or err.ipar
;---
iou_flnk
        moveq   #sms.info,d0             ; find system vars
        trap    #1
        move.l  sys_fsdl(a0),a0          ; ... and linked list of directory drvs
        move.l  a0,d1
        beq.s   iuf_ipar                 ; none at all!!
iuf_loop
        cmp.l   iod_dnam+2-iod_iolk(a0),d7 ; the right driver?
        beq.s   iuf_rts                  ; ... yes
        move.l  (a0),a0                  ; ... no, try the next
        move.l  a0,d1                    ; the last?
        bne.s   iuf_loop
iou_ipar
iuf_ipar
        moveq   #err.ipar,d0
iuf_rts
        rts
        end
