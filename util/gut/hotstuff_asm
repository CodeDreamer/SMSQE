* Hotkey Stuffer    V0.00     1987  Tony Tebby   QJUMP
*
        section gen_util
*
        xdef    gu_hotst
*
        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_sys'
*
hkd_pllk equ    $08     long    polling int linkage from A3
hkd_plad equ    $0c     long    polling int routine address from A3
hkd_id   equ    $10     long    hotkey ID
hkd.id   equ    'Hot%'
hkd_jobb equ    $14     long    hotkey job base address
hkd_next equ    $18     byte    next job (hotkey number pressed)
*
hkd_clos equ    $24     long    address of close
*
hkd_bufp equ    $3a     word    running pointer to buffer
hkd_bufl equ    $3c     word    length of buffer
hkd.bufl equ    $80
hkd_bufc equ    $3e     word    buffer byte counter
hkd_buff equ    $40             hotkey buffer
hkd.len  equ    hkd_buff        + hk_bufln is actual length of block
*+++
* Stuff one or two strings into the hotkey buffer, to be extracted by
* ALT-SPACE.  The D2/A2 string goes first, followed by the D3/A3 one.
*
*       d0  r   0
*       d1  r   length set
*       d2 c  p length of string (a2)
*       d3 c  p length of string (a3)
*       a2 c  p pointer to string characters (d2<>0)
*       a3 c  p pointer to string characters (d3<>0)
*
*       all other registers preserved
*---
gu_hotst
reglist reg     d2/d3/a0/a1/a2/a3
dreg    reg     d2/d3
*
        trap    #0                      supervisor mode
        movem.l reglist,-(sp)
        moveq   #sms.info,d0            get information
        trap    #1
*
        moveq   #0,d1                   preset nothing copied
*
        lea     sys_poll(a0),a0         polling list
hot_look
        move.l  (a0),d0                 next polling
        beq.s   hot_exit
        move.l  d0,a0
        cmp.l   #hkd.id,hkd_id-hkd_pllk(a0) our poll?
        bne.s   hot_look
*
        clr.w   hkd_bufc-hkd_pllk(a0)   no length
        move.w  #-1,hkd_bufp-hkd_pllk(a0) stop stuffing
        lea     hkd_buff-hkd_pllk(a0),a1 buffer address
        movem.l (sp),dreg               restore data registers
*
        bsr.s   hot_set                 set first string
        move.w  d3,d2
        move.l  a3,a2
        bsr.s   hot_set                 and second
*
        move.w  d1,hkd_bufc-hkd_pllk(a0) set count in buffer
*
hot_exit
        movem.l (sp)+,reglist
        and.w   #$dfff,sr
        moveq   #0,d0
        rts
*
*
hot_set
        add.w   d2,d1                   more characters
        move.w  hkd_bufl-hkd_pllk(a0),d0 space in buffer
        sub.w   d1,d0                   space left
        bge.s   hot_copy
        add.w   d0,d1                   move back a bit
        add.w   d0,d2
hot_copy
        move.w  d2,d0
        bra.s   hot_slend
hot_sloop
        move.b  (a2)+,(a1)+                     set characters
hot_slend
        dbra    d0,hot_sloop
        rts
        end
