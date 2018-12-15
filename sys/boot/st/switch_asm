; ATARI:  ROM switch

        section base

        include 'dev8_keys_k'

base
        move.w  $ffff8240,d6
        not.w   $ffff8240                ; change palette
        move.l  #200,d7
        add.l   $4ba,d7                  ; wait 1 sec

switch_wait
        not.w   $ffff8240                ; change palette

        moveq   #8,d5
switch_loop
        move.l  #$00010002,-(sp)         ; bconstat for con
        trap    #13
        addq.l  #4,sp
        tst.w   d0                       ; any character?
        dbne    d5,switch_loop
        bne.s   switch_check

        cmp.l   $4ba,d7                  ; timeout?
        bgt.s   switch_wait
        bra.s   switch_set

switch_check
        move.l  #$00020002,-(sp)         ; get character
        trap    #13
        addq.l  #4,sp
        cmp.b   #k.esc,d0                ; esc?
        beq.s   switch_rts               ; ... yes

switch_set
        not.w   d6
        move.w  d6,$ffff8240

        moveq   #0,d6
        move.b  d0,d6                    ; put key here, it is fairly safe
        bra.s   switch_end+8

switch_rts
        move.w d6,$ffff8240
        rts

switch_end
        end
