; RAM based RAM disk

        section base

        bra.l   rd_wmess

        section version

        section rd
        xref    rd_vmess
        xref    rd_vmend
        xref    rd_init
        xref    gu_smul

rd_wmess
        lea     rd_vmess,a1
        moveq   #rd_vmend-rd_vmess,d2
        sub.l   a0,a0
        jsr     gu_smul
        jmp     rd_init
        end
