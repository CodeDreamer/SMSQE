; Returns channel ID of SuperBASIC channel # or default #1

        section utility

        xdef    bi_gtchn
        xdef    bi_gtch0

        include dev8_keys_qlv
        include dev8_keys_err

;+++
; Returns channel ID of SuperBASIC channel # or default given #
;
;               Entry                           Exit
;       d0      default #
;       a0                                      channel ID
;       a1      arithmetic stack pointer        updated
;       a3      start of parameter pointer      updated to next parameter
;       a5      end of parameter pointer
;---
bi_gtch0
        move.l  d0,d7
        bra.s   bi_gtall

;+++
; Returns channel ID of SuperBASIC channel # or default #1
;
;               Entry                           Exit
;       a0                                      channel ID
;       a1      arithmetic stack pointer        updated
;       a3      start of parameter pointer      updated to next parameter
;       a5      end of parameter pointer
;---
bi_gtchn
         moveq    #1,d7             default channel no.
bi_gtall
         movem.l  a3/a5,-(sp)       save A3/A5
         moveq    #0,d0
         moveq    #0,d6             if default, no stack correction
         cmp.l    a3,a5             no parameter?
         beq.s    get_defc          yes, so use default
         move.l   a3,a5             let us check whether the ...
         addq.l   #8,a5             ... first parameter is preceeded ...
         btst     #7,1(a6,a3.l)     ... by a hash
         beq.s    get_defc          no, also use default
         move.w   sb.gtint,a2       yes, get integer value
         jsr      (a2)
         tst.l    d0                error, no integer?
         bne.s    get_erbp          yes, return error
         moveq    #0,d7
         moveq    #8,d6             we can correct the A3 pointer
         move.w   0(a6,a1.l),d7     get channel number
get_defc mulu     #$28,d7           calculate channel ID now
         move.l   $30(a6),a2
         add.l    d7,a2
         cmp.l    $34(a6),a2
         bhi.s    get_erno
         beq.s    get_erno
         tst.b    (a6,a2.l)             -ve?
         bmi.s    get_erno
         move.l   0(a6,a2.l),a0
         movem.l  (sp)+,a3/a5
         add.l    d6,a3
         rts
*
get_erno moveq    #err.ichn,d0
         bra.s    get_errt
get_erbp moveq    #err.ipar,d0
get_errt addq.l   #8,sp
         rts
*
         end
