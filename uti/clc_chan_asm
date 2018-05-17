; Calculate channel ID of SuperBASIC channel #    V1.01

        section utility

        xdef    bi_chnid
ch.lench equ    $28             ; channel entry length

        include dev8_keys_err
        include dev8_keys_bv
;+++
; Calculate channel ID of SuperBASIC channel #
;
;               Entry                           Exit
;       d1      channel #                       smashed
;       a0                                      channel ID
;       a2                                      smashed
;       a6      SuperBASIC master pointer       preserved
;
;       Error returns:  err.ichn
;                       err.ipar
;       Condition codes set on return
;---
bi_chnid
        mulu    #ch.lench,d1            ; find entry
        move.l  bv_chbas(a6),a2
        add.l   d1,a2
        cmp.l   bv_chp(a6),a2           ; out of table?
        bge.s   error_no
        move.l  0(a6,a2.l),a0           ; get channel ID
        moveq   #0,d0
        rts

error_no
        moveq   #err.ichn,d0
        rts

        end
