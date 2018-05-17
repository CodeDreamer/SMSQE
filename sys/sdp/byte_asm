* sdp send byte        V2.00     1987  Tony Tebby  QJUMP
*
        section sdp
*
        xdef    sdp_byte
        xdef    sdp_bytc
*
        xref    sdp_sbuf
*
        include dev8_sys_sdp_data
*
sdp_byte
;**        move.b  dp_escb(a5),d0           ; escape byte?
;**        beq.s   sdp_bytc
;**        cmp.b   d0,d1
;**        bne.s   sdp_bytc
;**        bsr.s   sdp_bytc
;**        nop
sdp_bytc
        add.w   #dp_bufpt,a5             ; prevent offset going out of range
        move.w  (a5),d0                  ; get buffer pointer
        move.b  d1,dp_buffr-dp_bufpt(a5,d0.w); put byte in
        addq.w  #1,d0                    ; next
        move.w  d0,(a5)                  ; saved
        sub.w   #dp_bufpt,a5             ; restore a5
        cmp.w   #dp.bufln,d0             ; full?
        bge.l   sdp_sbuf                 ; ... yes
        moveq   #0,d0                    ; no error
        rts
        end
