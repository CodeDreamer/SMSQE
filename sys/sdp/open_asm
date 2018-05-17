* Open SDUMP channel   V2.00     1985  Tony Tebby
*
        section sdp
*
        xdef    sdp_open
*
        include dev8_keys_qlv
*
io.name equ     $122
*
sdp_open
        move.l  a3,a4           save pointer to def block
        move.l  sp,a3           set pointer to parameters
        move.w  io.name,a2      read name
        jsr     (a2)
        bra.s   sdpo_exit
        bra.s   sdpo_exit
        bra.s   sdpo_setup
        dc.w    5,'SDUMP'
        dc.w    0
*
sdpo_setup
        moveq   #$20,d1         allocate $20 (minimum)
        move.w  mem.achp,a2     and allocate in heap
        jmp     (a2)
sdpo_exit
        rts
        end
