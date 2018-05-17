* Pointer Physical Routines  V1.03    1985   Tony Tebby   QJUMP
* Sandy SuperMouse
*
        section driver
*
        xdef    pt_start
        xdef    pointer
*
        xref    base
        xref    pt_init
        xref    pt_pbyte
*
        include dev8_keys_qdos_sms
        include dev8_keys_sys
        include dev8_keys_con
*
ms_stat  equ    $3fcc
ms_clear equ    $3fd0
*
ms.intxy equ    $ffffff00+%11000000
*
*
pt_start
        moveq   #sms.achp,d0            allocate initial linkage
        moveq   #pt_aext+4,d1
        moveq   #0,d2                   owned by zero
        trap    #1
        move.l  a0,a3
*
        moveq   #sms.lexi,d0            link in silly external interrupt
        lea     pt_lext(a3),a0
        lea     pti_xint(pc),a1
        move.l  a1,pt_aext(a3)
        trap    #1
        rts
*
        page
*
* external interrupt server for Sandy
*
pt_ext
        move.b  ms_stat+base(pc),d1     get port status register
        move.b  ms_clear+base(pc),d0    (clear interrupts)
        moveq   #ms.intxy,d0            check for interrupts
        and.b   d1,d0
        beq.s   pte_rts                 ... none
        addq.w  #1,pt_xicnt(a3)         ... yes, count it
*
****    tst.b   pt_pstat(a3)            is pointer visible?
****    bne.s   pte_rts                 ... no
*
        lsl.b   #1,d1                   get x interrupt bit
        bcc.s   pte_y_test              ... not set
        moveq   #1,d0                   assume add
        bclr    #6,d1                   is it up?
        beq.s   pte_x_move              ... no
        moveq   #-1,d0
pte_x_move
        add.w   d0,pt_xinc(a3)
pte_y_test
        lsl.b   #1,d1                   get y interrupt bit
        bcc.s   pte_rts                 ... not set
        moveq   #1,d0
        bclr    #6,d1                   is it left?
        beq.s   pte_y_move              ... no
        moveq   #-1,d0
pte_y_move
        add.w   d0,pt_yinc(a3)
pte_rts
        rts
        page
pt_poll
        clr.b   pt_kypol(a3)             ; newkey is clear
        moveq   #%00001110,d1            ; get button presses
        and.b   ms_stat+base,d1
        lsr.b   #1,d1                    ; look up table
        move.b  ptd_btab(pc,d1.w),d2
        bpl.s   ptds_snk                 ; not stuffer, set new key
*
        tas     pt_lstuf(a3)             ; was last press a stuff? (this one is)
        blt.s   ptds_rts                 ; yes, don't do another
        move.l  sys_ckyq(a6),a2          ; no, stuff something in here
        move.b  pt_stuf1(a3),d1          ; get first character
        beq.s   ptds_rts                 ; isn't one
        jsr     pt_pbyte(pc)             ; there is one, stuff it
        move.b  pt_stuf2(a3),d1          ; is there another?
        beq.s   ptds_rts                 ; no
        jmp     pt_pbyte(pc)             ; yes, stuff that and return
*
ptds_snk
        move.b  d2,pt_kypol(a3)          ; this is new key
        clr.b   pt_lstuf(a3)             ; and it isn't a stuff
ptds_rts
        rts
*
ptd_btab dc.b   1,$ff,1,1,2,2,3,0        ; button pressed (one is high priority)

        page
pointer
        moveq   #sms.info,d0            find sysvar
        trap    #1
        lea     sys_exil(a0),a0         look for our external interrupt
        lea     pti_xint(pc),a1
ptr_look
        move.l  (a0),d0                 next item in list
        beq.s   ptr_ok                  ... no more
        move.l  d0,a0
        cmp.l   4(a0),a1                us?
        bne.s   ptr_look 
*
        move.l  a0,-(sp)                dummy linkage found, initialise pointer
        bsr.l   pt_init
        move.l  (sp)+,a4
        bne.s   ptr_rts
*
        lea     pt_lext(a3),a0          link in new interrupt server
        lea     pt_ext(pc),a1
        move.l  a1,pt_aext(a3)
        moveq   #sms.lexi,d0
        trap    #1
        lea     pt_lpoll(a3),a0
        lea     pt_poll(pc),a1
        move.l  a1,pt_apoll(a3)
        moveq   #sms.lpol,d0
        trap    #1
*
        move.l  a4,a0                   remove old one
        moveq   #sms.rexi,d0
        trap    #1
        move.l  a4,a0
        moveq   #sms.rchp,d0            and throw away 
        trap    #1
ptr_ok
        moveq   #0,d0                   set OK
ptr_rts
        rts
*
pti_xint
        move.b  ms_clear+base(pc),d0    clear mouse interrupt
        rts
        end
